package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
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
                .nickname(request.getNickname())
                .profileOnOff(request.getProfileOnOff())
                .createdDate(LocalDateTime.now())
                .isAnswered(false)
                .build();
    }

    public QuestionDetailResponse toDomain(Question question, String fcmtoken) {
        return QuestionDetailResponse.of(
                question.getId(),
                question.getContent(),
                question.getNickname(),
                question.getProfileOnOff(),
                question.getCreatedDate(),
                fcmtoken,
                question.isAnswered()
        );
    }
}
