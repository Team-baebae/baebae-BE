package com.web.baebaeBE.presentation.question.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionDetailResponse {
    private Long questionId;
    private String content;
    private String userEmail;
    private LocalDateTime createdDate;
    private String token;

    public QuestionDetailResponse(Long questionId, String content, String userEmail, LocalDateTime createdDate, String token) {
        this.questionId = questionId;
        this.content = content;
        this.userEmail = userEmail;
        this.createdDate = createdDate;
        this.token = token;
    }
    public static QuestionDetailResponse of(Long questionId, String content, String userEmail, LocalDateTime createdDate, String token) {
        return new QuestionDetailResponse(questionId, content, userEmail, createdDate, token);
    }
}