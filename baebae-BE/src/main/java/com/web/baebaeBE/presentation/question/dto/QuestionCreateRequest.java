package com.web.baebaeBE.presentation.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionCreateRequest {
    private String content;
    public QuestionCreateRequest(String content) {

        this.content = content;
    }
    public static QuestionCreateRequest of(String content) {
        return new QuestionCreateRequest(content);
    }
}

