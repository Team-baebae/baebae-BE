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
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_date", nullable = false, length = 30)
    private LocalDateTime createdDate;

    public QuestionEntity(Long id, String content, Member member, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.createdDate = createdDate;
    }
    public void updateContent(String content) {
        this.content = content;
    }

}
