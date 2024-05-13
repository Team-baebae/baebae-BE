package com.web.baebaeBE.domain.answer.dto;

import com.web.baebaeBE.domain.answer.entity.Answer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponse {
    private Long id;
    private String content;
    private String nickname;

    public static AnswerResponse of(Answer answer) {
        AnswerResponse response = new AnswerResponse();
        response.id = answer.getId();
        response.content = answer.getContent();
        response.nickname = answer.getMember().getNickname();
        return response;
    }
}

