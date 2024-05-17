package com.web.baebaeBE.domain.reactioncount.entity;

import com.web.baebaeBE.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reaction_count")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReactionCount {
    @Id
    @Column(name = "answer_id")
    private Long answerId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(name = "heart_count", nullable = false)
    private int heartCount;

    @Column(name = "curious_count", nullable = false)
    private int curiousCount;

    @Column(name = "sad_count", nullable = false)
    private int sadCount;

    @Column(name = "connect_count", nullable = false)
    private int connectCount;

    public static ReactionCount of(Answer answer, int heartCount, int curiousCount, int sadCount, int connectCount) {
        ReactionCount reactionCount = new ReactionCount();
        reactionCount.setAnswer(answer);
        reactionCount.setHeartCount(heartCount);
        reactionCount.setCuriousCount(curiousCount);
        reactionCount.setSadCount(sadCount);
        reactionCount.setConnectCount(connectCount);
        return reactionCount;
    }
}
