package com.web.baebaeBE.presentation.question.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCreateRequest {
    private String content;
    public QuestionCreateRequest(String content) {
        this.content = content;
    }
    public static QuestionCreateRequest of(String content) {return new QuestionCreateRequest(content);
    }
}

