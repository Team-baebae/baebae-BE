package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerMapper;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.presentation.answer.dto.AnswerDetailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Transactional
    public AnswerDetailResponse createAnswer(AnswerCreateRequest request, Long memberId) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found with ID"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID"));

        Answer answer = answerMapper.toEntity(request, question, member);
        Answer savedAnswer = answerRepository.save(answer);
        return answerMapper.toDomain(savedAnswer);
    }

    @Transactional
    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Pageable pageable) {
        Page<Answer> answersPage = answerRepository.findAllByMemberId(memberId, pageable);
        return answersPage.map(answerMapper::toDomain);
    }

    @Transactional
    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with ID"));
        answer.setContent(request.getContent());
        answer.setLinkAttachment(request.getLinkAttachment());
        answer.setMusicSearch(request.getMusicSearch());
        answer.setImageFiles(request.getImageFiles());
        Answer updatedAnswer = answerRepository.save(answer);
        return answerMapper.toDomain(updatedAnswer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with ID"));
        answerRepository.delete(answer);
    }
}
