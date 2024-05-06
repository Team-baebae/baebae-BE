package com.web.baebaeBE.domain.category.service;

import com.web.baebaeBE.domain.category.exception.CategoryError;
import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.infra.categorized.answer.repository.CategorizedAnswerRepository;
import com.web.baebaeBE.infra.category.entity.Category;
import com.web.baebaeBE.infra.category.repository.CategoryRepository;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.presentation.category.dto.CategoryRequest;
import com.web.baebaeBE.presentation.category.dto.CategoryResponse;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
private final CategoryRepository categoryRepository;
private final CategorizedAnswerRepository categoryAnswerRepository;
private final MemberRepository memberRepository;
private final EntityManager entityManager; // Answer 엔티티 프록시 가져오기 위함.

    public Category createCategory(Long memberId, String categoryName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        Category category = Category.builder()
                .member(member)
                .categoryName(categoryName)
                .build();

        return categoryRepository.save(category);
    }

    public CategoryResponse.CategoryInformationResponse createAnswersToCategory(Long categoryId, List<Long> answerIds) {
        Category category = categoryRepository.findById(categoryId).get();
        //.orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));

        // 카테고리에 답변피드들 저장
        for(Long answerId : answerIds) {
            Answer answer = entityManager.getReference(Answer.class, answerId);
            categoryAnswerRepository.save(CategorizedAnswer.builder()
                    .category(category)
                    .answer(answer)
                    .build());
        }

        return CategoryResponse.CategoryInformationResponse.of(category);
    }
    public CategoryResponse.CategoryListResponse getCategoriesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        List<Category> categories = categoryRepository.findAllByMember(member);

        return CategoryResponse.CategoryListResponse.of(categories);
    }

    public CategoryResponse.CategoryInformationResponse updateAnswersToCategory(Long categoryId, List<Long> answerIds) {
        Category category = categoryRepository.findById(categoryId).get();
                //.orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));

        // 기존에 카테고리에 연결된 모든 CategorizedAnswer들을 조회
        List<CategorizedAnswer> oldCategorizedAnswers = categoryAnswerRepository.findAllByCategory(category);

        // 새로운 Answer들의 id들을 Set에 저장
        Set<Long> newAnswerIds = new HashSet<>(answerIds);

        // 기존의 CategorizedAnswer들 중 새로운 Answer들의 id들에 없는 것들을 찾아 삭제
        oldCategorizedAnswers.stream()
                .filter(categorizedAnswer -> !newAnswerIds.contains(categorizedAnswer.getAnswer().getId()))
                .forEach(categoryAnswerRepository::delete);

        // 새로운 Answer들의 id들 중 기존의 CategorizedAnswer들에 없는 것들을 찾아 추가
        oldCategorizedAnswers.stream()
                .map(CategorizedAnswer::getAnswer)
                .map(Answer::getId)
                .forEach(oldAnswerId -> {
                    if (!newAnswerIds.contains(oldAnswerId)) {
                        Answer answer = entityManager.getReference(Answer.class, oldAnswerId);
                        categoryAnswerRepository.save(CategorizedAnswer.builder()
                                .category(category)
                                .answer(answer)
                                .build());
                    }
                });

        return CategoryResponse.CategoryInformationResponse.of(category);
    }



    public CategoryResponse.CategoryInformationResponse updateCategoryName(Long categoryId, String categoryName) {
        Category category =  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));

        category.updateCategoryName(categoryName);
        categoryRepository.save(category);

        return CategoryResponse.CategoryInformationResponse.of(category);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
