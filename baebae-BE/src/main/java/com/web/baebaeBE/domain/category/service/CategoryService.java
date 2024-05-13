package com.web.baebaeBE.domain.category.service;

import com.web.baebaeBE.domain23.answer.exception.AnswerError;
import com.web.baebaeBE.domain.category.exception.CategoryException;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.categorized.answer.repository.CategorizedAnswerRepository;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.category.repository.CategoryRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.category.dto.CategoryResponse;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
private final CategoryRepository categoryRepository;
private final CategorizedAnswerRepository categoryAnswerRepository;
private final MemberRepository memberRepository;
private final AnswerRepository answerRepository;
private final EntityManager entityManager; // Answer 엔티티 프록시 가져오기 위함.

    public Category createCategory(Long memberId, MultipartFile categoryImage, String categoryName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        String imagePath = "default_image_path";
        if (categoryImage != null) {
            //imagePath = imageStorageService.save(categoryImage); //추후 수정 예정
        }

        Category category = Category.builder()
                .member(member)
                .categoryName(categoryName)
                .categoryImage(imagePath)
                .build();

        return categoryRepository.save(category);
    }

    public CategoryResponse.CategoryInformationResponse createAnswersToCategory(Long categoryId, List<Long> answerIds) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));

        // 카테고리에 Answer 추가
        for (Long answerId : answerIds) {
            Answer answer = answerRepository.findByAnswerId(answerId)
                    .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
            CategorizedAnswer categorizedAnswer = CategorizedAnswer.builder()
                    .category(category)
                    .answer(answer)
                    .build();
            category.getCategoryAnswers().add(categorizedAnswer); // CategorizedAnswer를 Category의 CategorizedAnswer 리스트에 추가
            categoryAnswerRepository.save(categorizedAnswer);
        }

        return CategoryResponse.CategoryInformationResponse.of(category);
    }
    public CategoryResponse.CategoryListResponse getCategoriesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        List<Category> categories = categoryRepository.findAllByMember(member);

        return CategoryResponse.CategoryListResponse.of(categories);
    }

    public Category updateCategoryName(Long categoryId, String categoryName) {
        Category category =  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));

        category.updateCategoryName(categoryName);
        return categoryRepository.save(category);
    }

    public CategoryResponse.CategoryInformationResponse updateCategoryImage(Long categoryId, MultipartFile imageFile) {
        // Category 엔티티 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));

        category.updateCategoryImage("default_image_path"); // 저장방식 추후 수정

        return CategoryResponse.CategoryInformationResponse.of(category);
    }
    public CategoryResponse.CategoryInformationResponse updateAnswersToCategory(Category category, List<Long> answerIds) {

        // 새로운 Answer들의 id들을 Set에 저장
        Set<Long> newAnswerIds = new HashSet<>(answerIds);

        // 기존의 CategorizedAnswer들 중 새로운 Answer들의 id들에 없는 것들을 찾아 삭제
        category.getCategoryAnswers().removeIf(categorizedAnswer -> {
            if (!newAnswerIds.contains(categorizedAnswer.getAnswer().getId())) {
                categoryAnswerRepository.delete(categorizedAnswer);
                return true;
            }
            return false;
        });

        // 새로운 Answer들의 id들 중 기존의 CategorizedAnswer들에 없는 것들을 찾아 추가
        newAnswerIds.forEach(newAnswerId -> {
            boolean isAlreadyAdded = category.getCategoryAnswers().stream()
                    .anyMatch(categorizedAnswer -> categorizedAnswer.getAnswer().getId().equals(newAnswerId));
            if (!isAlreadyAdded) {
                //Answer 엔티티 프록시 가져오기
                Answer answer = entityManager.getReference(Answer.class, newAnswerId);
                CategorizedAnswer newCategorizedAnswer = CategorizedAnswer.builder()
                        .category(category)
                        .answer(answer)
                        .build();
                // CategorizedAnswer 엔티티 저장
                category.getCategoryAnswers().add(newCategorizedAnswer);
                categoryAnswerRepository.save(newCategorizedAnswer);
            }
        });

        return CategoryResponse.CategoryInformationResponse.of(category);
    }

    public void addAnswerToCategory(Long categoryId, Long answerId) {
        // Category 엔티티 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));

        // Answer 엔티티 조회
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        // 이미 Category에 Answer가 있는지 확인
        boolean isAlreadyAdded = category.getCategoryAnswers().stream()
                .anyMatch(categorizedAnswer -> categorizedAnswer.getAnswer().equals(answer));
        if (isAlreadyAdded) {
            throw new BusinessException(CategoryException.ANSWER_ALREADY_EXIST);
        }

        // CategorizedAnswer 엔티티 생성 및 저장
        CategorizedAnswer categorizedAnswer = CategorizedAnswer.builder()
                .category(category)
                .answer(answer)
                .build();
        categoryAnswerRepository.save(categorizedAnswer);
    }
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
