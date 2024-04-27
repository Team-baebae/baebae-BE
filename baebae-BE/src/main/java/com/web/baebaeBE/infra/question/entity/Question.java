package com.web.baebaeBE.infra.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class Question {
    private Long questionId;
    private Long memberId;
    private String content;
    private LocalDateTime createdDate;
}

