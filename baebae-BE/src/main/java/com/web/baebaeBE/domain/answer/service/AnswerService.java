package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.global.error.exception.BusinessException;
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
    private final S3ImageStorageService s3ImageStorageService;

    public Answer createAnswer(Answer answer, List<MultipartFile> imageFiles) {
        List<String> urls = new ArrayList<>();
        try {
            int index = 0;
            String memberId = answer.getMember().getId().toString();
            String answerId = answer.getId().toString();

            for (MultipartFile file : imageFiles) {
                String fileType = "image";
                String fileName = "image_" + index++ + ".jpg";
                String path = memberId + "/" + answerId + "/" + fileName;

                InputStream inputStream = file.getInputStream();
                String url = s3ImageStorageService.uploadFile(memberId, answerId, fileType, index, inputStream, file.getSize(), file.getContentType());
                urls.add(url);
            }
        } catch (IOException e) {
            throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
        }
        answer.setImageFiles(urls);
        return answerRepository.save(answer);
    }

    @Transactional
    public Page<Answer> getAllAnswers(Long memberId, Pageable pageable) {
        return answerRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public Answer updateAnswer(Long answerId, AnswerCreateRequest request, MultipartFile[] imageFiles) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        List<String> urls = new ArrayList<>();
        try {
            String memberId = answer.getMember().getId().toString();
            String fileType = "image";
            int index = 0;

            for (MultipartFile file : imageFiles) {
                String fileName = "image_" + index + ".jpg";
                String path = memberId + "/" + answerId + "/" + fileName;

                InputStream inputStream = file.getInputStream();
                long size = file.getSize();
                String contentType = file.getContentType();
                String url = s3ImageStorageService.uploadFile(memberId, answerId.toString(), fileType, index, inputStream, size, contentType);
                urls.add(url);
                index++;
            }
        } catch (IOException e) {
            throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
        }
        answer.setImageFiles(urls);

        answer.setContent(request.getContent());
        answer.setLinkAttachments(request.getLinkAttachments());
        answer.setMusicName(request.getMusicName());

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
}
