package com.web.baebaeBE.infra.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class Question {
    private Long questionId;
    private Long memberId;
    private String content;
    public Question(Long questionId, Long memberId, String content) {
        this.questionId = questionId;
        this.memberId = memberId;
        this.content = content;
    }
}

