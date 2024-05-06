package com.web.baebaeBE.application.category;

import com.web.baebaeBE.domain.category.service.CategoryService;
import com.web.baebaeBE.infra.category.entity.Category;
import com.web.baebaeBE.presentation.category.dto.CategoryRequest;
import com.web.baebaeBE.presentation.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryApplication{

    private final CategoryService categoryService;

    public CategoryResponse.CategoryInformationResponse createCategory(Long memberId, CategoryRequest.CreateCategory createCategory) {
        Category category =
                categoryService.createCategory(memberId, createCategory.getCategoryName());

        return categoryService.createAnswersToCategory(category.getCategoryId(), createCategory.getAnswerIds());
    }
    public CategoryResponse.CategoryListResponse getCategoriesByMember(Long memberId) {
        return categoryService.getCategoriesByMember(memberId);
    }

    public CategoryResponse.CategoryInformationResponse updateCategoryName(Long categoryId, CategoryRequest.UpdateCategoryName updateCategoryName) {
        return categoryService.updateCategoryName(categoryId, updateCategoryName.getCategoryName());
    }

    public void deleteCategory(Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
