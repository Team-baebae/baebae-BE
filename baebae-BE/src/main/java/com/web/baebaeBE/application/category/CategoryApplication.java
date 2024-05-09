package com.web.baebaeBE.application.category;

import com.web.baebaeBE.domain.category.service.CategoryService;
import com.web.baebaeBE.infra.category.entity.Category;
import com.web.baebaeBE.presentation.category.dto.CategoryRequest;
import com.web.baebaeBE.presentation.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryApplication{

    private final CategoryService categoryService;

    public CategoryResponse.CategoryInformationResponse createCategory(Long memberId, MultipartFile categoryImage, CategoryRequest.CreateCategory createCategory) {
        Category category =
                categoryService.createCategory(memberId, categoryImage, createCategory.getCategoryName());

        return categoryService.createAnswersToCategory(category.getCategoryId(), createCategory.getAnswerIds());
    }
    public CategoryResponse.CategoryListResponse getCategoriesByMember(Long memberId) {
        return categoryService.getCategoriesByMember(memberId);
    }

    public void addAnswerToCategory(Long categoryId, Long answerId) {
        categoryService.addAnswerToCategory(categoryId, answerId);
    }

    public void updateCategoryImage(Long categoryId, MultipartFile imageFile) {
        categoryService.updateCategoryImage(categoryId, imageFile);
    }
    public CategoryResponse.CategoryInformationResponse updateCategory(Long categoryId, CategoryRequest.UpdateCategory updateCategory) {
        Category category = categoryService.updateCategoryName(categoryId, updateCategory.getCategoryName());
        return categoryService.updateAnswersToCategory(category, updateCategory.getAnswerIds());
    }



    public void deleteCategory(Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
