package com.web.baebaeBE.domain.category.controller;


import com.web.baebaeBE.domain.category.controller.api.CategoryApi;
import com.web.baebaeBE.domain.category.dto.CategoryRequest;
import com.web.baebaeBE.domain.category.dto.CategoryResponse;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.category.service.CategoryService;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationCategory;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationCategoryAndAnswer;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @PostMapping(value = "/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthorizationMember
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> createCategory(
            @PathVariable Long memberId,
            @RequestPart(value = "categoryImage", required = false) MultipartFile categoryImage,
            @RequestPart CategoryRequest.CreateCategory createCategory
    ) {
        Category category =
                categoryService.createCategory(memberId, categoryImage, createCategory.getCategoryName());

        return ResponseEntity.ok(categoryService.createAnswersToCategory(category.getId(), createCategory.getAnswerIds()));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<CategoryResponse.CategoryListResponse> getCategoriesByMember(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(categoryService.getCategoriesByMember(memberId));
    }

    @PostMapping("/{categoryId}/answers/{answerId}")
    @AuthorizationCategoryAndAnswer
    public ResponseEntity<Void> addAnswerToCategory(
            @PathVariable Long categoryId,
            @PathVariable Long answerId
    ) {

        categoryService.addAnswerToCategory(categoryId, answerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{categoryId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthorizationCategory
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategoryImage(
            @PathVariable Long categoryId,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {

        return ResponseEntity.ok(categoryService.updateCategoryImage(categoryId, imageFile));
    }


    @PutMapping("/{categoryId}")
    @AuthorizationCategory
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest.UpdateCategory updateCategory
    ) {

        Category category = categoryService.updateCategoryName(categoryId, updateCategory.getCategoryName());
        return ResponseEntity.ok(categoryService.updateAnswersToCategory(category, updateCategory.getAnswerIds()));
    }

    @DeleteMapping("/{categoryId}")
    @AuthorizationCategory
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
