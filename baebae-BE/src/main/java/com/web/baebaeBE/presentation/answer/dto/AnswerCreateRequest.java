package com.web.baebaeBE.presentation.answer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerCreateRequest {
    private Long questionId;
    private String content;
    private String linkAttachment;
    private String musicSearch;
    private List<String> imageFiles;

    public AnswerCreateRequest(Long questionId, String content, String linkAttachment, String musicSearch, List<String> imageFiles) {
        this.questionId = questionId;
        this.content = content;
        this.linkAttachment = linkAttachment;
        this.musicSearch = musicSearch;
        this.imageFiles = imageFiles;
    }

    public static AnswerCreateRequest of(Long questionId, String content, String linkAttachment, String musicSearch, List<String> imageFiles) {
        return new AnswerCreateRequest(questionId, content, linkAttachment, musicSearch, imageFiles);
    }
}
