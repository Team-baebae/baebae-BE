package com.web.baebaeBE.domain.question.service;

import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
import com.web.baebaeBE.domain.question.exception.QuestionError;
import com.web.baebaeBE.domain.question.repository.QuestionMapper;
import com.web.baebaeBE.global.error.exception.BusinessException;
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
    private final NotificationService notificationService;

    @Transactional
    public QuestionDetailResponse createQuestion(QuestionCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        Question question = questionMapper.toEntity(request, member);
        Question savedQuestion = questionRepository.save(question);

        // 알림생성
        NotificationRequest.create notificationDto = new NotificationRequest.create(
                member.getId(),
                "새로운 질문이 등록되었습니다!",
                question.getContent(),
                NotificationRequest.EventType.NEW_QUESTION,
                null // reactionType
        );

        notificationService.createNotification(notificationDto);

        return questionMapper.toDomain(savedQuestion, member.getFcmToken());
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getQuestionsByMemberId(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByMemberId(memberId, pageable);
        return questions.map(question -> questionMapper.toDomain(question, question.getMember().getFcmToken()));
    }

    @Transactional
    public QuestionDetailResponse updateQuestion(Long questionId, String content, Boolean isAnswered) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        // 내용과 답변 상태 업데이트
        question.updateContent(content);
        question.setAnswered(isAnswered);
        // 질문 업데이트 후 저장
        Question updatedQuestion = questionRepository.save(question);

        return questionMapper.toDomain(updatedQuestion, updatedQuestion.getMember().getFcmToken());

    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));
        questionRepository.delete(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getAnsweredQuestions(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByMemberIdAndIsAnsweredTrue(memberId, pageable);
        return questions.map(question -> questionMapper.toDomain(question, question.getMember().getFcmToken()));
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getUnansweredQuestions(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByMemberIdAndIsAnsweredFalse(memberId, pageable);
        return questions.map(question -> questionMapper.toDomain(question, question.getMember().getFcmToken()));
    }

    @Transactional(readOnly = true)
    public long countQuestionsByMemberId(Long memberId) {
        return questionRepository.countByMemberId(memberId);
    }
}