package com.web.baebaeBE.domain.question.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class QuestionUpdateRequest {
    private String content;
    private String nickname;

    public QuestionUpdateRequest(String content,String nickname) {
        this.content = content;
        this.nickname = nickname;
    }
    public static QuestionUpdateRequest of( String content, String nickname) {
        return new QuestionUpdateRequest(content, nickname);
    }
}
