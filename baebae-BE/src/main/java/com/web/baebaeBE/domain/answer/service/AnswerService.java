package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Transactional
    public Page<Answer> getAllAnswers(Long memberId, Pageable pageable) {
        return answerRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public Answer updateAnswer(Long answerId, AnswerCreateRequest request) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        answer.setContent(request.getContent());
        answer.setLinkAttachment(request.getLinkAttachment());
        answer.setMusicSearch(request.getMusicSearch());
        answer.setImageFiles(request.getImageFiles());

        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answerEntity = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("No answer found with id " + answerId));
        answerRepository.delete(answerEntity);
    }
}
