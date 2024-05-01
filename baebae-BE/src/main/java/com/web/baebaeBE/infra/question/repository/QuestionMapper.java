package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    public Question toEntity(QuestionCreateRequest request, Member member) {
        return Question.builder()
                .member(member)
                .content(request.getContent())
                .createdDate(LocalDateTime.now())
                .build();
    }

    public QuestionDetailResponse toDomain(Question question) {
        return QuestionDetailResponse.of(
                question.getId(),
                question.getContent(),
                question.getMember().getNickname(),
                question.getCreatedDate());
    }
}
