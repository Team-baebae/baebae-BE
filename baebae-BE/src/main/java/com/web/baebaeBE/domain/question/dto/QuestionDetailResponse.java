package com.web.baebaeBE.domain.question.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionDetailResponse {
    private Long questionId;
    private String content;
    private String nickname;
    private Boolean profileOnOff;
    private LocalDateTime createdDate;
    private Boolean isAnswered;

    public QuestionDetailResponse(Long questionId, String content, String nickname, Boolean profileOnOff, LocalDateTime createdDate, Boolean isAnswered) {
        this.questionId = questionId;
        this.content = content;
        this.nickname = nickname;
        this.profileOnOff = profileOnOff;
        this.createdDate = createdDate;
        this.isAnswered = isAnswered;
    }
    public static QuestionDetailResponse of(Long questionId, String content, String nickname, Boolean profileOnOff, LocalDateTime createdDate, Boolean isAnswered) {
        return new QuestionDetailResponse(questionId, content, nickname, profileOnOff, createdDate, isAnswered);
    }
}
