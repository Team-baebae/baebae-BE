package com.web.baebaeBE.domain.answer.repository;

import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.music.entity.Music;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AnswerMapper {
    public Answer toEntity(AnswerCreateRequest request, Question question, Member member) {
        // Music 엔티티 생성
        Music music = Music.builder()
                .musicName(request.getMusicName())
                .musicSinger(request.getMusicSinger())
                .musicAudioUrl(request.getMusicAudioUrl())
                .build();

        // Answer 엔티티 생성 및 Music 설정
        Answer answer = Answer.builder()
                .question(question)
                .member(member)
                .nickname(request.getNickname())
                .content(request.getContent())
                .linkAttachments(request.getLinkAttachments())
                .profileOnOff(request.getProfileOnOff())
                .createdDate(LocalDateTime.now())
                .heartCount(0)
                .curiousCount(0)
                .sadCount(0)
                .connectCount(0)
                .music(music)
                .build();

        // Music 엔티티에 Answer 설정
        music.setAnswer(answer);

        return answer;
    }

    public AnswerDetailResponse toDomain(Answer answer) {
        Music music = answer.getMusic();
        Member member = answer.getMember();
        Question question = answer.getQuestion();
        List<String> imageFiles = answer.getImageFiles();
        String imageUrl = (imageFiles != null && !imageFiles.isEmpty()) ? imageFiles.get(0) : null;

        return AnswerDetailResponse.of(
                answer.getId(),
                question.getId(),
                question.getContent(),
                member.getId(),
                answer.getContent(),
                member.getNickname(),
                answer.getNickname(),
                answer.isProfileOnOff(),
                answer.getLinkAttachments(),
                music != null ? music.getMusicName() : null,
                music != null ? music.getMusicSinger() : null,
                music != null ? music.getMusicAudioUrl() : null,
                imageUrl,
                answer.getCreatedDate()

        );
    }

    public AnswerResponse toResponse(Answer answer) {
        return AnswerResponse.of(answer);
    }
}
