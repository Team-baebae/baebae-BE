package com.web.baebaeBE.infra.question.entity;

import com.web.baebaeBE.infra.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_onoff", nullable = false)
    private Boolean profileOnOff;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public void updateContent(String content) {
        this.content = content;
    }
    public static Question of(Long id, Member member, String content, String nickname, Boolean profileOnOff,
                              LocalDateTime createdDate) {
        return new Question(id, member, content, nickname, profileOnOff, createdDate);
    }
}

