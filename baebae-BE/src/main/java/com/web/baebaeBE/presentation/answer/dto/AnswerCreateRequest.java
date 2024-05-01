package com.web.baebaeBE.presentation.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerCreateRequest {
    private Long questionId;
    private String content;
    private String linkAttachment;
    private String musicSearch;
    private List<String> imageUrls;
    private MultipartFile[] imageFiles;

    public AnswerCreateRequest(Long questionId, String content, String linkAttachment, String musicSearch, List<String> imageUrls, MultipartFile[] imageFiles) {
        this.questionId = questionId;
        this.content = content;
        this.linkAttachment = linkAttachment;
        this.musicSearch = musicSearch;
        this.imageUrls = imageUrls;
        this.imageFiles = imageFiles;
    }

    public static AnswerCreateRequest of(Long questionId, String content, String linkAttachment, String musicSearch, List<String> imageUrls, MultipartFile[] imageFiles) {
        return new AnswerCreateRequest(questionId, content, linkAttachment, musicSearch, imageUrls, imageFiles);
    }
}