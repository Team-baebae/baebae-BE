package com.web.baebaeBE.domain.answer.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerCreateRequest {
    private Long questionId;
    private String content;
    private List<String> linkAttachments;
    private String musicName;
    private String musicSinger;
    private String musicPicture;

    public AnswerCreateRequest(Long questionId, String content, List<String> linkAttachments,
                               String musicName, String musicSinger, String musicPicture) {
        this.questionId = questionId;
        this.content = content;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.musicPicture = musicPicture;
    }

    public static AnswerCreateRequest of(Long questionId, String content, List<String> linkAttachments,
                                         String musicName, String musicSinger, String musicPicture) {
        return new AnswerCreateRequest(questionId, content, linkAttachments, musicName, musicSinger,
                 musicPicture);
    }
}
