package com.web.baebaeBE.domain.question.service;

import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
import com.web.baebaeBE.domain.question.exception.QuestionError;
import com.web.baebaeBE.domain.question.repository.QuestionMapper;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.firebase.FirebaseNotificationService;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final QuestionMapper questionMapper;
    private final FirebaseNotificationService firebaseNotificationService;

    @Transactional
    public QuestionDetailResponse createQuestion(QuestionCreateRequest request, Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        Question question = questionMapper.toEntity(request, sender, receiver);
        Question savedQuestion = questionRepository.save(question);

        firebaseNotificationService.notifyNewQuestion(member, question); // 파이어베이스 메세지 송신


        return questionMapper.toDomain(savedQuestion);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getQuestionsByMemberId(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByReceiverId(memberId, pageable);
        return questions.map(question -> questionMapper.toDomain(question));
    }

    @Transactional
    public QuestionDetailResponse updateQuestion(Long questionId, String content) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        question.updateContent(content);
        Question updatedQuestion = questionRepository.save(question);

        //firebaseNotificationService.notifyNewQuestion(updatedQuestion.getSender(), updatedQuestion); // 파이어베이스 메세지 송신

        return questionMapper.toDomain(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        questionRepository.delete(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getAnsweredQuestions(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByReceiverIdAndIsAnsweredTrue(memberId, pageable);
        return questions.map(question -> questionMapper.toDomain(question));
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getUnansweredQuestions(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByReceiverIdAndIsAnsweredFalse(memberId, pageable);
        return questions.map(question -> questionMapper.toDomain(question));
    }

    @Transactional(readOnly = true)
    public long getUnansweredQuestionCount(Long memberId) {
        return questionRepository.countByReceiverIdAndIsAnsweredFalse(memberId);
    }

}