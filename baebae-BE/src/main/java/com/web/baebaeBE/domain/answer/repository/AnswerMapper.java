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

@Component
@AllArgsConstructor
public class AnswerMapper {
    public Answer toEntity(AnswerCreateRequest request, Question question, Member member) {
        // 요청에서 music 관련 필드가 있는지 확인
        Music music = null;
        if (request.getMusicName() != null || request.getMusicSinger() != null || request.getMusicAudioUrl() != null) {
            // Music 엔티티 생성
            music = Music.builder()
                    .musicName(request.getMusicName())
                    .musicSinger(request.getMusicSinger())
                    .musicAudioUrl(request.getMusicAudioUrl())
                    .build();
        }

        String senderNickname = question.getNickname();

        // Answer 엔티티 생성 및 Music 설정
        Answer answer = Answer.builder()
                .question(question)
                .member(member)
                .content(request.getContent())
                .nickname(senderNickname)
                .linkAttachments(request.getLinkAttachments())
                .profileOnOff(request.getProfileOnOff())
                .createdDate(LocalDateTime.now())
                .music(music) // music이 null이 아닌 경우 설정
                .build();

        // Music 엔티티에 Answer 설정 (music이 null이 아닌 경우)
        if (music != null) {
            music.setAnswer(answer);
        }

        return answer;
    }

    public AnswerDetailResponse toDomain(Answer answer) {
        Music music = answer.getMusic();
        Member member = answer.getMember();
        Question question = answer.getQuestion();

        return AnswerDetailResponse.of(
                answer.getId(),
                question.getId(),
                question.getContent(),
                member.getId(),
                answer.getContent(),
                answer.getNickname(),
                question.getSender().getNickname(),
                answer.isProfileOnOff(),
                answer.getLinkAttachments(),
                music != null ? music.getMusicName() : null,
                music != null ? music.getMusicSinger() : null,
                music != null ? music.getMusicAudioUrl() : null,
                answer.getImageFile(),
                answer.getCreatedDate()
        );
    }

    public AnswerResponse toResponse(Answer answer) {
        return AnswerResponse.of(answer);
    }
}
