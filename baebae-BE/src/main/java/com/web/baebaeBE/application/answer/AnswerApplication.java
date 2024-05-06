package com.web.baebaeBE.application.answer;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.exception.BusinessException;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AnswerApplication {
    private final AnswerService answerService;
    private final AnswerMapper answerMapper;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    public AnswerDetailResponse createAnswer(AnswerCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_QUESTION));

        Answer answerEntity = answerMapper.toEntity(request, question, member);
        Answer savedAnswerEntity = answerService.createAnswer(answerEntity, request.getImageFiles());
        return answerMapper.toDomain(savedAnswerEntity);
    }

    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Pageable pageable) {
        Page<Answer> answerPage = answerService.getAllAnswers(memberId, pageable);
        return answerPage.map(answerMapper::toDomain);
    }

    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request, MultipartFile[] imageFiles) {
        Answer updatedAnswer = answerService.updateAnswer(answerId, request, imageFiles);
        return answerMapper.toDomain(updatedAnswer);
    }

    public void deleteAnswer(Long answerId) {
        answerService.deleteAnswer(answerId);
    }
}