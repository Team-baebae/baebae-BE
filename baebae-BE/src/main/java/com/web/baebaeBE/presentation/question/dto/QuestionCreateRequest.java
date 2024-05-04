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
    private String nickname;
    private Boolean profileOnOff;
    public QuestionCreateRequest(String content,String nickname, Boolean profileOnOff) {
        this.content = content;
        this.nickname = nickname;
        this.profileOnOff = profileOnOff;
    }
    public static QuestionCreateRequest of(String content, String nickname, Boolean profileOnOff) {
        return new QuestionCreateRequest(content, nickname, profileOnOff);
    }
}

