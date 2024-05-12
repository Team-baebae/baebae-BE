package com.web.baebaeBE.domain.category.controller;


import com.web.baebaeBE.domain.category.controller.api.CategoryApi;
import com.web.baebaeBE.domain.category.dto.CategoryRequest;
import com.web.baebaeBE.domain.category.dto.CategoryResponse;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.category.service.CategoryService;
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
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> createCategory(
            @PathVariable Long memberId,
            @RequestPart(value = "categoryImage", required = false) MultipartFile categoryImage,
            @RequestPart CategoryRequest.CreateCategory createCategory
    ) {
        Category category =
                categoryService.createCategory(memberId, categoryImage, createCategory.getCategoryName());

        return ResponseEntity.ok(categoryService.createAnswersToCategory(category.getCategoryId(), createCategory.getAnswerIds()));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<CategoryResponse.CategoryListResponse> getCategoriesByMember(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(categoryService.getCategoriesByMember(memberId));
    }

    @PostMapping("/{categoryId}/answers/{answerId}")
    public ResponseEntity<Void> addAnswerToCategory(
            @PathVariable Long categoryId,
            @PathVariable Long answerId
    ) {

        categoryService.addAnswerToCategory(categoryId, answerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{categoryId}/image")
    public ResponseEntity<Void> updateCategoryImage(
            @PathVariable Long categoryId,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {

        categoryService.updateCategoryImage(categoryId, imageFile);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest.UpdateCategory updateCategory
    ) {

        Category category = categoryService.updateCategoryName(categoryId, updateCategory.getCategoryName());
        return ResponseEntity.ok(categoryService.updateAnswersToCategory(category, updateCategory.getAnswerIds()));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
