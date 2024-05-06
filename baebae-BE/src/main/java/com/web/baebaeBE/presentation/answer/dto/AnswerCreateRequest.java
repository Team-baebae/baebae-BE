package com.web.baebaeBE.presentation.answer.dto;

import org.springframework.web.multipart.MultipartFile;
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
    private String musicAudio;
    private List<String> imageUrls; // 이미지 URL 리스트
    private List<MultipartFile> imageFiles; // MultipartFile 리스트, 파일 업로드용

    public AnswerCreateRequest(Long questionId, String content, List<String> linkAttachments,
                               String musicName, String musicSinger, String musicAudio,
                               List<String> imageUrls, List<MultipartFile> imageFiles) {
        this.questionId = questionId;
        this.content = content;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.musicAudio = musicAudio;
        this.imageUrls = imageUrls;
        this.imageFiles = imageFiles;
    }

    public static AnswerCreateRequest of(Long questionId, String content, List<String> linkAttachments,
                                         String musicName, String musicSinger, String musicAudio,
                                         List<String> imageUrls, List<MultipartFile> imageFiles) {
        return new AnswerCreateRequest(questionId, content, linkAttachments, musicName, musicSinger,
                musicAudio, imageUrls,imageFiles);
    }
}
