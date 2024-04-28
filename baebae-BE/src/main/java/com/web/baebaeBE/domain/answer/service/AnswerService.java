package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.presentation.answer.dto.AnswerDetailResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class AnswerService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }
    @Transactional
    public Answer createAnswer(AnswerCreateRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found with ID"));

        Answer answer = Answer.builder()
                .question(question)
                .content(request.getContent())
                .linkAttachment(request.getLinkAttachment())
                .musicSearch(request.getMusicSearch())
                .imageFiles(request.getImageFiles())
                .createdDate(LocalDateTime.now())
                .likeCount(0)
                .build();

        return answerRepository.save(answer);
    }

    @Transactional
    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Pageable pageable) {
        Page<Answer> answersPage = answerRepository.findAllByMemberId(memberId, pageable);
        return answersPage.map(answer -> AnswerDetailResponse.of(answer.getId(), answer.getQuestion().getId(),
                answer.getMember().getId(), answer.getContent(),answer.getLinkAttachment(), answer.getMusicSearch(),
                answer.getImageFiles(), answer.getCreatedDate(), answer.getLikeCount()));

    }

    @Transactional
    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with ID: " + answerId));

        answer.setContent(request.getContent());
        answer.setLinkAttachment(request.getLinkAttachment());
        answer.setMusicSearch(request.getMusicSearch());
        answer.setImageFiles(request.getImageFiles());

        Answer updatedAnswer = answerRepository.save(answer);
        return AnswerDetailResponse.of(
                updatedAnswer.getId(),
                updatedAnswer.getQuestion().getId(),
                updatedAnswer.getMember().getId(),
                updatedAnswer.getContent(),
                updatedAnswer.getLinkAttachment(),
                updatedAnswer.getMusicSearch(),
                updatedAnswer.getImageFiles(),
                updatedAnswer.getCreatedDate(),
                updatedAnswer.getLikeCount());
    }
    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with ID: " + answerId));
        answerRepository.delete(answer);
    }
}
