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
    public Question toEntity(QuestionCreateRequest request, Member sender, Member receiver) {
        return Question.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .nickname(request.getNickname())
                .profileOnOff(request.getProfileOnOff())
                .createdDate(LocalDateTime.now())
                .isAnswered(false)
                .build();
    }

    public QuestionDetailResponse toDomain(Question question) {
        Member sender = question.getSender();
        return QuestionDetailResponse.of(
                question.getId(),
                question.getContent(),
                question.getNickname(),
                sender.getNickname(),
                question.getProfileOnOff(),
                question.getCreatedDate(),
                question.isAnswered()
        );
    }
}