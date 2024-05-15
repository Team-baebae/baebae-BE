package com.web.baebaeBE.domain.answer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AnswerDetailResponse {
    private Long answerId;
    private Long questionId;
    private String questionContent;
    private Long memberId;
    private String content;
    private String memberNickname;
    private String nickname;
    private Boolean profileOnOff;
    private String linkAttachments;
    private String musicName;
    private String musicSinger;
    private String musicAudioUrl;
    private LocalDateTime createdDate;
    private Integer heartCount;
    private Integer curiousCount;
    private Integer sadCount;

    public AnswerDetailResponse(Long answerId, Long questionId, String questionContent, Long memberId,
                                String content, String memberNickname, String nickname, Boolean profileOnOff,
                                String linkAttachments, String musicName, String musicSinger, String musicAudioUrl,
                                 LocalDateTime createdDate,
                                Integer heartCount, Integer curiousCount, Integer sadCount) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.memberId = memberId;
        this.content = content;
        this.memberNickname = memberNickname;
        this.nickname = nickname;
        this.profileOnOff = profileOnOff;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.musicAudioUrl = musicAudioUrl;
        this.createdDate = createdDate;
        this.heartCount = heartCount;
        this.curiousCount = curiousCount;
        this.sadCount = sadCount;
    }

    public static AnswerDetailResponse of(Long answerId, Long questionId, String questionContent, Long memberId,
                                          String content, String memberNickname, String nickname, Boolean profileOnOff,
                                          String linkAttachments, String musicName, String musicSinger, String musicAudioUrl,
                                           LocalDateTime createdDate,
                                          Integer heartCount, Integer curiousCount, Integer sadCount) {
        return new AnswerDetailResponse(answerId, questionId, questionContent, memberId, content, memberNickname,
                nickname, profileOnOff, linkAttachments, musicName, musicSinger, musicAudioUrl, createdDate,
                heartCount, curiousCount, sadCount);

    }
}
