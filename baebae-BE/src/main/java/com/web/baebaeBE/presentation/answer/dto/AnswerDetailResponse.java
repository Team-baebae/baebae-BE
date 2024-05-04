package com.web.baebaeBE.presentation.answer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AnswerDetailResponse {
    private Long answerId;
    private Long questionId;
    private Long memberId;
    private String content;
    private List<String> linkAttachments;
    private String musicName;
    private String musicPictureUrl;
    private String musicAudioUrl;
    private List<String> imageUrls;
    private LocalDateTime createdDate;
    private Integer heartCount;
    private Integer curiousCount;
    private Integer sadCount;

    public AnswerDetailResponse(Long answerId, Long questionId, Long memberId, String content,
                                List<String> linkAttachments, String musicName, String musicPictureUrl,
                                String musicAudioUrl, List<String> imageUrls, LocalDateTime createdDate,
                                Integer heartCount, Integer curiousCount, Integer sadCount) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.memberId = memberId;
        this.content = content;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicPictureUrl = musicPictureUrl;
        this.musicAudioUrl = musicAudioUrl;
        this.imageUrls = imageUrls;
        this.createdDate = createdDate;
        this.heartCount = heartCount;
        this.curiousCount = curiousCount;
        this.sadCount = sadCount;
    }

    public static AnswerDetailResponse of(Long answerId, Long questionId, Long memberId, String content,
                                          List<String> linkAttachments, String musicName, String musicPictureUrl,
                                          String musicAudioUrl, List<String> imageUrls, LocalDateTime createdDate,
                                          Integer heartCount, Integer curiousCount, Integer sadCount) {
        return new AnswerDetailResponse(answerId, questionId, memberId, content, linkAttachments, musicName,
                musicPictureUrl, musicAudioUrl, imageUrls, createdDate,
                heartCount, curiousCount, sadCount);
    }
}
