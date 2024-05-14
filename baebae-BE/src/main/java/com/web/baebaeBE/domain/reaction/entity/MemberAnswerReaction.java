package com.web.baebaeBE.domain.reaction.entity;


import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_answer_reaction")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberAnswerReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction", nullable = false)
    private ReactionValue reaction;

}
