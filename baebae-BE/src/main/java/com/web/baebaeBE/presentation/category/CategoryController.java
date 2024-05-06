package com.web.baebaeBE.presentation.category;


import com.web.baebaeBE.application.category.CategoryApplication;
import com.web.baebaeBE.presentation.category.api.CategoryApi;
import com.web.baebaeBE.presentation.category.dto.CategoryRequest;
import com.web.baebaeBE.presentation.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController implements CategoryApi {

    private final CategoryApplication categoryApplication;

    @PostMapping("/{memberId}")
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> createCategory(
            @PathVariable Long memberId,
            @RequestBody CategoryRequest.CreateCategory createCategory
    ) {
        return ResponseEntity.ok(categoryApplication.createCategory(memberId, createCategory));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<CategoryResponse.CategoryListResponse> getCategoriesByMember(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(categoryApplication.getCategoriesByMember(memberId));
    }

    @PostMapping("/{categoryId}/answers/{answerId}")
    public ResponseEntity<Void> addAnswerToCategory(
            @PathVariable Long categoryId,
            @PathVariable Long answerId
    ) {
        categoryApplication.addAnswerToCategory(categoryId, answerId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{categoryId}/image")
    public ResponseEntity<Void> updateCategoryImage(
            @PathVariable Long categoryId,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        categoryApplication.updateCategoryImage(categoryId, imageFile);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest.UpdateCategory updateCategory
    ) {
        return ResponseEntity.ok(categoryApplication.updateCategory(categoryId, updateCategory));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {
        categoryApplication.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
