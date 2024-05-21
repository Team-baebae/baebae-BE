package com.web.baebaeBE.domain.answer.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class AnswerCreateRequest {
    private Long questionId;
    private Boolean profileOnOff;
    private String content;
    private String linkAttachments;
    private String musicName;
    private String musicSinger;
    private String musicAudioUrl;
    private String imageUrl;
    private boolean updateImage;



    public AnswerCreateRequest(Long questionId, String content,
                               Boolean profileOnOff, String linkAttachments,
                               String musicName, String musicSinger, String musicAudioUrl, String imageUrl, boolean updateImage) {
        this.questionId = questionId;
        this.content = content;
        this.profileOnOff = profileOnOff;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.musicAudioUrl = musicAudioUrl;
        this.imageUrl = imageUrl;
        this.updateImage = updateImage;

    }

    public static AnswerCreateRequest of(Long questionId, String content,
                                         Boolean profileOnOff, String linkAttachments,
                                         String musicName, String musicSinger, String musicAudioUrl, String imageUrl, boolean updateImage) {
        return new AnswerCreateRequest(questionId, content,  profileOnOff, linkAttachments,
                musicName, musicSinger, musicAudioUrl, imageUrl, updateImage);
    }
}