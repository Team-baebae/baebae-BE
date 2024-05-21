package com.web.baebaeBE.domain.categorized.answer.service;

import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.repository.AnswerMapper;
import com.web.baebaeBE.domain.answer.repository.AnswerRepository;
import com.web.baebaeBE.domain.categorized.answer.dto.CategorizedAnswerRequest;
import com.web.baebaeBE.domain.categorized.answer.dto.CategorizedAnswerResponse;
import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.categorized.answer.repository.CategorizedAnswerRepository;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.category.exception.CategoryException;
import com.web.baebaeBE.domain.category.repository.CategoryRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CategorizedAnswerService {
    private final CategorizedAnswerRepository categorizedAnswerRepository;
    private final AnswerMapper answerMapper;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;

    public List<CategorizedAnswerResponse.CategoryInformationResponse> getCategoriesByAnswerId(Long answerId) {
        List<CategorizedAnswer> categorizedAnswers = categorizedAnswerRepository.findAllByAnswerId(answerId);

        for(CategorizedAnswer categorizedAnswer : categorizedAnswers) {
            log.info("categorizedAnswer : {}", categorizedAnswer.getCategory().getId());
        }
        List<CategorizedAnswerResponse.CategoryInformationResponse> categoryInformationResponses = new ArrayList<>();

        for(CategorizedAnswer categorizedAnswer : categorizedAnswers) {
            categoryInformationResponses.add
                    (CategorizedAnswerResponse.CategoryInformationResponse.of(categorizedAnswer.getCategory()));
        }

        return categoryInformationResponses;
    }


    public Page<AnswerDetailResponse> getAnswersByMemberAndCategory(Long memberId, Long categoryId, Pageable pageable) {
        Page<CategorizedAnswer> categorizedAnswers;
        if (categoryId == null) { // 카테고리 정보가 없을 경우 -> 전체 피드 조회
            categorizedAnswers = categorizedAnswerRepository.findByAnswer_Member_Id(memberId, pageable);
        } else  // 카테고리 정보가 있을 경우 -> 해당 카테고리 피드 조회
            categorizedAnswers = categorizedAnswerRepository.findByAnswer_Member_IdAndCategory_Id(memberId, categoryId, pageable);

        return categorizedAnswers.map(categorizedAnswer -> answerMapper.toDomain(categorizedAnswer.getAnswer()));
    }


    @Transactional
    public void updateCategoriesByAnswerId(Long answerId, CategorizedAnswerRequest.CategoryList categoryList) {
        List<CategorizedAnswer> existingCategorizedAnswers = categorizedAnswerRepository.findAllByAnswerId(answerId);
        List<Long> existingCategoryIds = existingCategorizedAnswers.stream()
                .map(categorizedAnswer -> categorizedAnswer.getCategory().getId())
                .collect(Collectors.toList());

        List<Long> newCategoryIds = categoryList.getCategoryIds();

        // 추가해야 할 카테고리 ID 계산
        List<Long> toAddCategoryIds = newCategoryIds.stream()
                .filter(categoryId -> !existingCategoryIds.contains(categoryId))
                .collect(Collectors.toList());

        // 삭제해야 할 카테고리 ID 계산
        List<Long> toRemoveCategoryIds = existingCategoryIds.stream()
                .filter(categoryId -> !newCategoryIds.contains(categoryId))
                .collect(Collectors.toList());

        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        // 새로운 카테고리 ID에 대해 반복하여 CategorizedAnswer를 생성하고 저장
        for (Long categoryId : toAddCategoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));
            CategorizedAnswer newCategorizedAnswer = CategorizedAnswer.builder()
                    .category(category)
                    .answer(answer)
                    .build();
            categorizedAnswerRepository.save(newCategorizedAnswer);
        }

        // 삭제해야 할 카테고리 ID에 대해 반복하여 해당 CategorizedAnswer를 삭제
        for (Long categoryId : toRemoveCategoryIds) {
            CategorizedAnswer categorizedAnswerToRemove = existingCategorizedAnswers.stream()
                    .filter(categorizedAnswer -> categorizedAnswer.getCategory().getId().equals(categoryId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No CategorizedAnswer found for the given category id"));
            categorizedAnswerRepository.delete(categorizedAnswerToRemove);
        }
    }
}