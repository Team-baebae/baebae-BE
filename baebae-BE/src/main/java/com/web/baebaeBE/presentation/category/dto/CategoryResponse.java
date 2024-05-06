package com.web.baebaeBE.presentation.category.dto;

import com.web.baebaeBE.infra.category.entity.Category;
import lombok.*;

import java.util.List;

public class CategoryResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInformationResponse {
        private Long categoryId;
        private String categoryName;
        private List<Long> answerAnswers;

        public static CategoryResponse.CategoryInformationResponse of(Category category) {
            return CategoryInformationResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .answerAnswers(category.getCategoryAnswers().stream().map(answer -> answer.getId()).toList())
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
