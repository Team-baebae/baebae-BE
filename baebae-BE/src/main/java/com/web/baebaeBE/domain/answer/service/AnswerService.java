package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.firebase.FirebaseNotificationService;
import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final FirebaseNotificationService firebaseNotificationService;
    private final S3ImageStorageService s3ImageStorageService;

    @Transactional
    public Answer createAnswer(Answer answer, List<MultipartFile> imageFiles, MultipartFile audioFile) {
        List<String> imageUrls = new ArrayList<>();
        String musicAudioUrl = null;

        answer = answerRepository.save(answer);

        try {
            // 이미지 파일 업로드
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);
                if (!file.isEmpty()) {
                    InputStream inputStream = file.getInputStream();
                    String imageUrl = s3ImageStorageService.uploadFile(answer.getMember().getId().toString(), answer.getId().toString(), "image", i, inputStream, file.getSize(), file.getContentType());
                    imageUrls.add(imageUrl);
                }
            }

            // 오디오 파일 업로드
            if (audioFile != null && !audioFile.isEmpty()) {
                InputStream inputStream = audioFile.getInputStream();
                musicAudioUrl = s3ImageStorageService.uploadFile(answer.getMember().getId().toString(), answer.getId().toString(), "audio", 0, inputStream, audioFile.getSize(), audioFile.getContentType());
            }

            // 파일 URL을 Answer 엔티티에 저장
            answer.setImageFiles(imageUrls);
            answer.setMusicAudio(musicAudioUrl);

        } catch (Exception e) {
            // 롤백 이후 파일 삭제 로직
            imageUrls.forEach(url -> s3ImageStorageService.deleteFileByUrl(url)); // 이미지 파일 삭제
            if (musicAudioUrl != null) {
                s3ImageStorageService.deleteFileByUrl(musicAudioUrl); // 오디오 파일 삭제
            }
            throw new RuntimeException("Failed to create answer", e);
        }

        return answer;
    }

    @Transactional
    public Page<Answer> getAllAnswers(Long memberId, Pageable pageable) {
        return answerRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public Answer updateAnswer(Long answerId, AnswerCreateRequest request, MultipartFile[] imageFiles, MultipartFile audioFile) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        List<String> urls = new ArrayList<>();
        try {
            String memberId = answer.getMember().getId().toString();
            String fileType = "image";
            int index = 0;

            // 이미지 파일 처리
            for (MultipartFile file : imageFiles) {
                String fileName = "image_" + index + ".jpg";
                InputStream inputStream = file.getInputStream();
                long size = file.getSize();
                String contentType = file.getContentType();
                String url = s3ImageStorageService.uploadFile(memberId, answerId.toString(), fileType, index, inputStream, size, contentType);
                urls.add(url);
                index++;
            }

            // 오디오 파일 처리
            if (audioFile != null && !audioFile.isEmpty()) {
                InputStream inputStream = audioFile.getInputStream();
                long size = audioFile.getSize();
                String contentType = audioFile.getContentType();
                String musicAudioUrl = s3ImageStorageService.uploadFile(memberId, answerId.toString(), "audio", 0, inputStream, size, contentType);
                answer.setMusicAudio(musicAudioUrl);
            }

        } catch (IOException e) {
            throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
        }

        // 이미지 URL 리스트 업데이트
        answer.setImageFiles(urls);

        // 다른 필드 업데이트
        answer.setContent(request.getContent());
        answer.setLinkAttachments(request.getLinkAttachments());
        answer.setMusicName(request.getMusicName());
        answer.setMusicSinger(request.getMusicSinger());

        // 데이터베이스 저장
        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("No answer found with id " + answerId));

        answer.getImageFiles().forEach(url -> {
            String[] parts = url.split("/");
            if (parts.length > 1) {
                String fileKey = parts[parts.length - 1];
                String[] keyParts = fileKey.split("_");
                if (keyParts.length > 1) {
                    int index = Integer.parseInt(keyParts[1].replace(".jpg", ""));
                    String memberId = parts[parts.length - 4].split("_")[1];
                    String fileType = keyParts[0];

                    s3ImageStorageService.deleteFile(memberId, answerId.toString(), fileType, index);
                }
            }
        });
        answerRepository.delete(answer);
    }

    @Transactional
    public void updateReactionCounts(Long answerId, int heartCount, int curiousCount, int sadCount) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        // 반응수 올라갈때마다 알림 전송
        if (answer.getHeartCount() != heartCount) {
            firebaseNotificationService.notifyReaction(answer.getMember(), answer.getContent(), "heart");
        }
        if (answer.getCuriousCount() != curiousCount) {
            firebaseNotificationService.notifyReaction(answer.getMember(), answer.getContent(), "curious");
        }
        if (answer.getSadCount() != sadCount) {
            firebaseNotificationService.notifyReaction(answer.getMember(), answer.getContent(), "sad");
        }

        // 반응 수 업데이트
        answer.setHeartCount(heartCount);
        answer.setCuriousCount(curiousCount);
        answer.setSadCount(sadCount);
        answerRepository.save(answer);
    }
}
