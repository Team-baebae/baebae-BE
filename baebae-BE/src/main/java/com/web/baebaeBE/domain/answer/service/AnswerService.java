package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.infra.image.s3.S3ImageStorageService;
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

    public Answer createAnswer(Answer answer, MultipartFile[] imageFiles) {
        List<String> urls = new ArrayList<>();
        try {
            for (MultipartFile file : imageFiles) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String path = "answers/images";
                InputStream inputStream = file.getInputStream();
                String url = s3ImageStorageService.uploadFile(path, fileName, inputStream, file.getSize(), file.getContentType());
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
            for (MultipartFile file : imageFiles) {
                String fileName = file.getOriginalFilename();
                String path = "answers/images";
                InputStream inputStream = file.getInputStream();
                String url = s3ImageStorageService.uploadFile(path, fileName, inputStream, file.getSize(), file.getContentType());
                urls.add(url);
            }
        } catch (IOException e) {
            throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
        }
        answer.setImageFiles(urls);

        answer.setContent(request.getContent());
        answer.setLinkAttachment(request.getLinkAttachment());
        answer.setMusicSearch(request.getMusicSearch());

        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("No answer found with id " + answerId));
        answer.getImageFiles().forEach(url -> {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            s3ImageStorageService.deleteFile("answers/images", fileName);
        });
        answerRepository.delete(answer);
    }
}
