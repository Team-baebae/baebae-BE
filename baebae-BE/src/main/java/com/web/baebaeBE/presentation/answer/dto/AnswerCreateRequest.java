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
    private String musicAudioUrl;
    private List<String> imageUrls; // 이미지 URL 리스트

    public AnswerCreateRequest(Long questionId, String content, List<String> linkAttachments,
                               String musicName, String musicSinger, String musicAudioUrl,
                               List<String> imageUrls) {
        this.questionId = questionId;
        this.content = content;
        this.linkAttachments = linkAttachments;
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.musicAudioUrl = musicAudioUrl;
        this.imageUrls = imageUrls;
    }

    public static AnswerCreateRequest of(Long questionId, String content, List<String> linkAttachments,
                                         String musicName, String musicSinger, String musicAudioUrl,
                                         List<String> imageUrls) {
        return new AnswerCreateRequest(questionId, content, linkAttachments, musicName, musicSinger,
                musicAudioUrl, imageUrls);
    }
}