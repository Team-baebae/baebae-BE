package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private final MemberRepository memberRepository;

    public QuestionEntity toEntity(Question question, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));

        return QuestionEntity.builder()
                .content(question.getContent())
                .member(member)
                .build();
    }

    public QuestionEntity toEntity(Question question, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No member found with id: " + memberId));

        return QuestionEntity.builder()
                .content(question.getContent())
                .member(member)
                .build();
    }

    public Question toDomain(QuestionEntity questionEntity) {
        return new Question(
                questionEntity.getId(),
                questionEntity.getMember().getId(),
                questionEntity.getContent()
        );
    }
}

