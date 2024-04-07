package com.web.baebaeBE.domain.example.dto;

import com.web.baebaeBE.domain.example.domain.Example;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RequestExample {

    @Getter
    public static class Post {

        private Long exampleId;
        private String exampleContent;

        public Example toEntity(){
            return Example.builder()
                    .exampleId(exampleId)
                    .exampleContent(exampleContent)
                    .build();
        }
    }
}
