package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private MemberRepository memberRepository;

    public QuestionEntity toEntity(Question question, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));

        return QuestionEntity.builder()
                .content(question.getContent())
                .member(member)
                .build();
    }

    public QuestionDetailResponse toDomain(QuestionEntity questionEntity) {
        return new QuestionDetailResponse(
                questionEntity.getId(),
                questionEntity.getContent(),
                questionEntity.getMember().getNickname(),
                questionEntity.getCreatedDate()
        );
    }
}

