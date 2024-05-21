package com.web.baebaeBE.domain.categorized.answer.dto;

import com.web.baebaeBE.domain.category.dto.CategoryResponse;
import com.web.baebaeBE.domain.category.entity.Category;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CategorizedAnswerResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInformationResponse {
        private Long categoryId;
        private String categoryName;
        private String categoryImage;

        public static CategorizedAnswerResponse.CategoryInformationResponse of(Category category) {
            return CategorizedAnswerResponse.CategoryInformationResponse.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getCategoryName())
                    .categoryImage(category.getCategoryImage())
                    .build();
        }
    }

}
