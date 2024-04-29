package com.web.baebaeBE.application.answer;

import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerMapper;
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

@Service
@RequiredArgsConstructor
public class AnswerApplication {
    private final AnswerService answerService;
    private final AnswerMapper answerMapper;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    public AnswerDetailResponse createAnswer(AnswerCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No member found with id " ));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("No question found with id "));
        Answer answerEntity = answerMapper.toEntity(request, question, member);
        Answer savedAnswerEntity = answerService.createAnswer(answerEntity);
        return answerMapper.toDomain(savedAnswerEntity);
    }

    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Pageable pageable) {
        Page<Answer> answerPage = answerService.getAllAnswers(memberId, pageable);
        return answerPage.map(answerMapper::toDomain);
    }

    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request) {
        Answer updatedAnswer = answerService.updateAnswer(answerId, request);
        return answerMapper.toDomain(updatedAnswer);
    }

    public void deleteAnswer(Long answerId) { answerService.deleteAnswer(answerId);
    }
}
