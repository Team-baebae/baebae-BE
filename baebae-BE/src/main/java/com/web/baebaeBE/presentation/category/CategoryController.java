package com.web.baebaeBE.presentation.category;


import com.web.baebaeBE.application.category.CategoryApplication;
import com.web.baebaeBE.presentation.category.api.CategoryApi;
import com.web.baebaeBE.presentation.category.dto.CategoryRequest;
import com.web.baebaeBE.presentation.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategoryName(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest.UpdateCategoryName updateCategoryName
    ) {
        return ResponseEntity.ok(categoryApplication.updateCategoryName(categoryId, updateCategoryName));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {
        categoryApplication.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
