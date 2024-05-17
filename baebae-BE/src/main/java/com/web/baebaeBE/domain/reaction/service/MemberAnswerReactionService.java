package com.web.baebaeBE.domain.reaction.service;


import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.repository.AnswerRepository;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.reactioncount.dto.ReactionResponse;
import com.web.baebaeBE.domain.reaction.entity.MemberAnswerReaction;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import com.web.baebaeBE.domain.reaction.exception.ReactionException;
import com.web.baebaeBE.domain.reaction.repository.MemberAnswerReactionRepository;
import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import com.web.baebaeBE.domain.reactioncount.repository.ReactionCountJpaRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAnswerReactionService {

    private final ReactionUpdateService reactionUpdateService;
    private final MemberAnswerReactionRepository memberAnswerReactionRepository;
    private final ReactionCountJpaRepository reactionCountJpaRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;

    public ReactionResponse.ReactionInformationDto createReaction(Long memberId, Long answerId, ReactionValue reaction) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
        boolean isClicked = false;

        // ReactionCount 엔터티를 가져옴
        ReactionCount reactionCount = reactionCountJpaRepository.findByAnswerId(answerId);
        if (reactionCount == null) {
            throw new BusinessException(AnswerError.NO_EXIST_ANSWER);
        }

        // 이미 해당 반응이 있는지 확인
        Optional<MemberAnswerReaction> existingReactionOpt = memberAnswerReactionRepository.findByMemberAndAnswerAndReaction(member, answer, reaction);

        if (existingReactionOpt.isPresent()) {
            // 이미 해당 반응이 있다면 반응을 삭제
            MemberAnswerReaction existingReaction = existingReactionOpt.get();
            memberAnswerReactionRepository.delete(existingReaction);
            reactionUpdateService.decreaseReactionCount(reactionCount, reaction);
            isClicked = false;
        } else {
            // 해당 반응이 없다면 새로운 반응을 저장
            MemberAnswerReaction memberAnswerReaction = MemberAnswerReaction.builder()
                    .member(member)
                    .answer(answer)
                    .reaction(reaction)
                    .build();

            memberAnswerReactionRepository.save(memberAnswerReaction);
            reactionUpdateService.increaseReactionCount(reactionCount, reaction);
            isClicked = true;
        }

        return ReactionResponse.ReactionInformationDto.of(reactionCount, isClicked);
    }

    /*// 피드 주인이 아닌 다른 사람이 통했당 신청
    public ReactionResponse.ConnectionReactionInformationDto createConnectionReaction(Long memberId, Long answerId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ReactionException.NOT_EXIST_MEMBER));
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(ReactionException.NOT_EXIST_ANSWER));
        boolean isClicked = false;

        Optional<MemberAnswerReaction> existingReactionOpt = memberAnswerReactionRepository.findByMemberAndAnswerAndReaction(member, answer, ReactionValue.CONNECTION);
        if(existingReactionOpt.isPresent()){
            // 이미 해당 반응이 있다면 반응을 삭제
            MemberAnswerReaction existingReaction = existingReactionOpt.get();
            memberAnswerReactionRepository.delete(existingReaction);
        }else{// 해당 반응이 없다면 상대방 반응유무 체크후 통했당 저장
            MemberAnswerReaction memberAnswerReaction = MemberAnswerReaction.builder()
                    .member(member)
                    .answer(answer)
                    .reaction(ReactionValue.CONNECTION) // 통했당
                    .build();

            memberAnswerReactionRepository.save(memberAnswerReaction);
            isClicked = true;
        }

        return ReactionResponse.ConnectionReactionInformationDto.of(isClicked, false);
    }

    //피드 주인이 통했당을 연결할 때,
    public ReactionResponse.ConnectionReactionInformationDto connectConnectionReaction(Long memberId, Long answerId, Long destinationMemberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ReactionException.NOT_EXIST_MEMBER));
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(ReactionException.NOT_EXIST_ANSWER));
        Member destinationMember = memberRepository.findById(destinationMemberId)
                .orElseThrow(() -> new BusinessException(ReactionException.NOT_EXIST_MEMBER));

        // (상대방이 해당 피드에 통했당을 남겼는지 체크
        Optional<MemberAnswerReaction> existingCheckOpt = memberAnswerReactionRepository.findByMemberAndAnswerAndReaction(destinationMember, answer, ReactionValue.CONNECTION);

        // 상대방의 통했당 신청이 있는지 확인
        if (existingCheckOpt.isPresent()) {
            memberAnswerReactionRepository.delete(existingCheckOpt.get()); // 상대방 통했당 신청 삭제 후 연결
            return ReactionResponse.ConnectionReactionInformationDto.of(true,true);
        } else {
            throw new BusinessException(ReactionException.NOT_EXIST_CONNECTION_REACTION);
        }
    }*/
}