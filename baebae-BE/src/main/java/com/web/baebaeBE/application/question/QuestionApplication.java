package com.web.baebaeBE.application.question;

import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.domain.question.service.QuestionService;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionMapper;
import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionApplication {
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final MemberRepository memberRepository;

    public QuestionDetailResponse createQuestion(QuestionCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        Question questionEntity = questionMapper.toEntity(request, member);
        Question savedQuestionEntity = questionService.createQuestion(questionEntity);
        return questionMapper.toDomain(savedQuestionEntity);
    }

    public Page<QuestionDetailResponse> getAllQuestions(Long memberId, Pageable pageable) {
        Page<Question> questionPage = questionService.getAllQuestions(memberId, pageable);
        return questionPage.map(questionMapper::toDomain);
    }

    public QuestionDetailResponse updateQuestion(Long questionId, String content) {
        Question updatedQuestion = questionService.updateQuestion(questionId, content);
        return questionMapper.toDomain(updatedQuestion);
    }

    public void deleteQuestion(Long questionId) {
        questionService.deleteQuestion(questionId);
    }
}
