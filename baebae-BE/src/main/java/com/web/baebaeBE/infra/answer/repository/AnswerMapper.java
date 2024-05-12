package com.web.baebaeBE.infra.answer.repository;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.presentation.answer.dto.AnswerDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AnswerMapper {
    public Answer toEntity(AnswerCreateRequest request, Question question, Member member) {
        return Answer.builder()
                .question(question)
                .member(member)
                .content(request.getContent())
                .linkAttachments(request.getLinkAttachments()) // 링크 첨부 리스트 처리
                .musicName(request.getMusicName())
                .musicSinger(request.getMusicSinger())
                .musicAudio(request.getMusicAudioUrl())
                .createdDate(LocalDateTime.now())
                .heartCount(0)
                .curiousCount(0)
                .sadCount(0)
                .build();
    }

    public AnswerDetailResponse toDomain(Answer answer, String fcmtoken) {
        return AnswerDetailResponse.of(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getQuestion().getContent(), //질문 내용
                answer.getMember().getId(),
                answer.getContent(),
                answer.getLinkAttachments(),
                answer.getMusicName(),
                answer.getMusicSinger(),
                answer.getMusicAudio(),
                answer.getImageFiles(),
                answer.getCreatedDate(),
                answer.getHeartCount(),
                answer.getCuriousCount(),
                answer.getSadCount(),
                fcmtoken
        );
    }
}