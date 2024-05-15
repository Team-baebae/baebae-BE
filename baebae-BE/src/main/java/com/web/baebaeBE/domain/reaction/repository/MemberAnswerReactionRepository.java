package com.web.baebaeBE.domain.reaction.repository;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.reaction.entity.MemberAnswerReaction;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAnswerReactionRepository extends JpaRepository<MemberAnswerReaction, Long> {
    Optional<MemberAnswerReaction> findByMemberAndAnswerAndReaction(Member member, Answer answer, ReactionValue reaction);
    Optional<MemberAnswerReaction> findByMemberIdAndAnswerId(Long memberId, Long answerId);
}
