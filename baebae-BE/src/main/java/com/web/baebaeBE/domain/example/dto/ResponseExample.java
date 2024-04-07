package com.web.baebaeBE.domain.example.dto;

import com.web.baebaeBE.domain.example.domain.Example;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseExample {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long exampleId;
        private String exampleContent;

        public static Response of(Example example){
            return Response.builder()
                    .exampleId(example.getExampleId())
                    .exampleContent(example.getExampleContent())
                    .build();
        }
    }
}
