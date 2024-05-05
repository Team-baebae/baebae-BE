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
    public Question toEntity(QuestionCreateRequest request, Member sender, Member receiver) {
        return Question.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .nickname(request.getNickname())
                .profileOnOff(request.getProfileOnOff())
                .createdDate(LocalDateTime.now())
                .build();
    }

    public QuestionDetailResponse toDomain(Question question, String token) {
        return QuestionDetailResponse.of(
                question.getId(),
                question.getContent(),
                question.getNickname(),
                question.getCreatedDate(),
                token
        );
    }
}