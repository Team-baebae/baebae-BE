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
                .content(request.getContent())
                .linkAttachment(request.getLinkAttachment())
                .musicSearch(request.getMusicSearch())
                .imageFiles(request.getImageUrls())
                .createdDate(LocalDateTime.now())
                .likeCount(0)
                .build();
    }

    public AnswerDetailResponse toDomain(Answer answer) {
        return AnswerDetailResponse.of(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getMember().getId(),
                answer.getContent(),
                answer.getLinkAttachment(),
                answer.getMusicSearch(),
                answer.getImageFiles(),
                answer.getCreatedDate(),
                answer.getLikeCount());
    }
}
