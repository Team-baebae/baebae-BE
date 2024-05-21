package com.web.baebaeBE.domain.answer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDetailResponse {
    private Long answerId;
    private Long questionId;
    private String questionContent;
    private Long memberId;
    private String content;
    private String nickname;
    private Boolean profileOnOff;
    private String linkAttachments;
    private String musicName;
    private String musicSinger;
    private String musicAudioUrl;
    private String imageUrl;
    private LocalDateTime createdDate;


    public AnswerDetailResponse(Long answerId, Long questionId, String questionContent, Long memberId,
                                String content, String nickname, Boolean profileOnOff,
                                String linkAttachments, String musicName, String musicSinger, String musicAudioUrl,
                                 String imageUrl, LocalDateTime createdDate) {

                               
        this.answerId = answerId;
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.memberId = memberId;
        this.content = content;
        this.nickname = nickname;
        this.profileOnOff = profileOnOff;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.musicAudioUrl = musicAudioUrl;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
    }

    public static AnswerDetailResponse of(Long answerId, Long questionId, String questionContent, Long memberId,
                                          String content, String nickname, Boolean profileOnOff,
                                          String linkAttachments, String musicName, String musicSinger, String musicAudioUrl,
                                          String imageUrl, LocalDateTime createdDate) {
        return new AnswerDetailResponse(answerId, questionId, questionContent, memberId, content, nickname,
                 profileOnOff, linkAttachments, musicName, musicSinger, musicAudioUrl, imageUrl, createdDate);

      
    }
}
