package com.web.baebaeBE.domain.category.dto;

import com.web.baebaeBE.domain.category.entity.Category;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInformationResponse {
        private Long categoryId;
        private String categoryName;
        private String categoryImage;
        private List<Long> answerIds;

        public static CategoryResponse.CategoryInformationResponse of(Category category) {
            return CategoryInformationResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .categoryImage(category.getCategoryImage())
                    .answerIds(category.getCategoryAnswers().stream()
                            .map(categorizedAnswer -> categorizedAnswer.getAnswer().getId())
                            .collect(Collectors.toList()))
                    .build();
        }
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryListResponse {
        private List<CategoryResponse.CategoryInformationResponse> categories;

        public static CategoryResponse.CategoryListResponse of(List<Category> categories) {
            return CategoryListResponse.builder()
                    .categories(categories.stream().map(CategoryResponse.CategoryInformationResponse::of).toList())
                    .build();
        }

    }
}
