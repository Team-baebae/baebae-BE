package com.web.baebaeBE.infra.answer.repository;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.member.entity.Member;
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
                .imageFiles(request.getImageUrls()) // 이미지 URL 리스트 처리
                .content(request.getContent())
                .linkAttachments(request.getLinkAttachments()) // 링크 첨부 리스트 처리
                .musicName(request.getMusicName())
                .musicPicture(request.getMusicPicture())
                .musicAudio(request.getMusicAudio())
                .createdDate(LocalDateTime.now())
                .heartCount(0) // 초기 감정 횟수 설정
                .curiousCount(0) // 초기 궁금함 횟수
                .sadCount(0) // 초기 슬픔 횟수
                .build();
    }

    public AnswerDetailResponse toDomain(Answer answer) {
        return AnswerDetailResponse.of(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getMember().getId(),
                answer.getContent(),
                answer.getLinkAttachments(),
                answer.getMusicName(),
                answer.getMusicPicture(),
                answer.getMusicAudio(),
                answer.getImageFiles(),
                answer.getCreatedDate(),
                answer.getHeartCount(),
                answer.getCuriousCount(),
                answer.getSadCount()
        );
    }
}