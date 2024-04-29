package com.web.baebaeBE.domain.question.service;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.question.exception.QuestionError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionMapper;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Page<Question> getAllQuestions(Long memberId, Pageable pageable) {
        return questionRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public Question updateQuestion(Long questionId, String content) {
        Question questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        questionEntity.updateContent(content);
        return questionRepository.save(questionEntity);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("No question found with id"));
        questionRepository.delete(questionEntity);
    }

}
