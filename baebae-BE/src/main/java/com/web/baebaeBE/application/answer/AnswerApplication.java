package com.web.baebaeBE.application.answer;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerMapper;
import com.web.baebaeBE.infra.image.s3.S3ImageStorageService;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.presentation.answer.dto.AnswerDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AnswerApplication {
    private final AnswerService answerService;
    private final AnswerMapper answerMapper;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final S3ImageStorageService s3ImageStorageService;

    public AnswerDetailResponse createAnswer(AnswerCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_QUESTION));
        List<String> imageUrls = uploadImages(request.getImageFiles());
        Answer answerEntity = answerMapper.toEntity(request, question, member);
        Answer savedAnswerEntity = answerService.createAnswer(answerEntity, request.getImageFiles());
        return answerMapper.toDomain(savedAnswerEntity);
    }

    private List<String> uploadImages(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String path = "answers/images";
                InputStream inputStream = file.getInputStream();
                String url = s3ImageStorageService.uploadFile(path, fileName, inputStream, file.getSize(), file.getContentType());
                urls.add(url);
            } catch (IOException e) {
                System.err.println("Error uploading file: " + e.getMessage());
                throw new RuntimeException("Failed to upload image files", e);
            }
        }
        return urls;
    }

    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Pageable pageable) {
        Page<Answer> answerPage = answerService.getAllAnswers(memberId, pageable);
        return answerPage.map(answerMapper::toDomain);
    }

    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request, MultipartFile[] imageFiles) {
        Answer updatedAnswer = answerService.updateAnswer(answerId, request, imageFiles);
        return answerMapper.toDomain(updatedAnswer);
    }

    public void deleteAnswer(Long answerId) { answerService.deleteAnswer(answerId);
    }


}
