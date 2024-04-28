package com.web.baebaeBE.domain.question.service;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, MemberRepository memberRepository) {
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public QuestionDetailResponse createQuestion(Long memberId, QuestionCreateRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No member found with id"));

        QuestionEntity questionEntity = QuestionEntity.builder()
                .member(member)
                .content(request.getContent())
                .createdDate(LocalDateTime.now())
                .build();

        QuestionEntity savedQuestionEntity = questionRepository.save(questionEntity);
        return QuestionDetailResponse.of(
                savedQuestionEntity.getId(),
                savedQuestionEntity.getContent(),
                member.getNickname(),
                savedQuestionEntity.getCreatedDate());
    }

    @Transactional(readOnly = true)
    public Page<QuestionDetailResponse> getAllQuestions(Long memberId, Pageable pageable) {
        Page<QuestionEntity> questionsPage = questionRepository.findAllByMemberId(memberId, pageable);
        return questionsPage.map(question -> QuestionDetailResponse.of(question.getId(), question.getContent(),
                question.getMember().getEmail(),
                question.getCreatedDate()));
    }


    @Transactional
    public QuestionDetailResponse updateQuestion(Long questionId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Question content cannot be empty.");
        }

        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("No question found with id"));

        questionEntity.updateContent(content);
        QuestionEntity updatedQuestionEntity = questionRepository.save(questionEntity);
        return QuestionDetailResponse.of(
                updatedQuestionEntity.getId(),
                updatedQuestionEntity.getContent(),
                updatedQuestionEntity.getMember().getNickname(),
                updatedQuestionEntity.getCreatedDate());
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("No question found with id"));
        questionRepository.delete(questionEntity);
    }


}
