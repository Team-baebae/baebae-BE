package com.web.baebaeBE.presentation.answer.dto;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
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
    private String linkAttachment;
    private String musicSearch;
    private List<String> imageFiles;
    private LocalDateTime createdDate;
    private Integer likeCount;

    public AnswerDetailResponse(Long answerId, Long questionId, Long memberId, String content, String linkAttachment,
                                String musicSearch, List<String> imageFiles, LocalDateTime createdDate, Integer likeCount) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.memberId = memberId;
        this.content = content;
        this.linkAttachment = linkAttachment;
        this.musicSearch = musicSearch;
        this.imageFiles = imageFiles;
        this.createdDate = createdDate;
        this.likeCount = likeCount;
    }

    public static AnswerDetailResponse of(Long answerId, Long questionId, Long memberId, String content, String linkAttachment,
                                          String musicSearch, List<String> imageFiles, LocalDateTime createdDate, Integer likeCount) {
        return new AnswerDetailResponse(answerId, questionId, memberId, content, linkAttachment,
                musicSearch, imageFiles, createdDate, likeCount);
    }
}
