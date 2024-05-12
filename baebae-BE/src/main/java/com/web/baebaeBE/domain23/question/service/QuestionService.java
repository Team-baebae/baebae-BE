package com.web.baebaeBE.domain23.question.service;

import com.web.baebaeBE.domain23.question.exception.QuestionError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.firebase.FirebaseNotificationService;
import com.web.baebaeBE.infra.question.entity.Question;
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
    private final FirebaseNotificationService firebaseNotificationService;

    @Transactional
    public Question createQuestion(Question question) {
        Question savedQuestion = questionRepository.save(question);
        // 질문 생성 후 알림 발송
        firebaseNotificationService.notifyNewQuestion(savedQuestion.getMember(), savedQuestion);
        return savedQuestion;
    }

    @Transactional(readOnly = true)
    public Page<Question> getQuestionsByMemberId(Long memberId, Pageable pageable) {
        return questionRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public Question updateQuestion(Long questionId, String content) {
        Question questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        questionEntity.updateContent(content);
        Question updatedQuestion = questionRepository.save(questionEntity);
        // 질문 업데이트 후 알림 발송
        firebaseNotificationService.notifyNewQuestion(updatedQuestion.getMember(), updatedQuestion);
        return updatedQuestion;
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        questionRepository.delete(questionEntity);
    }

}