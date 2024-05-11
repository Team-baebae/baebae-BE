package com.web.baebaeBE.presentation.category.dto;


import com.web.baebaeBE.infra.member.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public class CategoryRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCategory{
        private String categoryName;
        private List<Long> answerIds;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCategory{
        private String categoryName;
        private List<Long> answerIds;
    }

}
