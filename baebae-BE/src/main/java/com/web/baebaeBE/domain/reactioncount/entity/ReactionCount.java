package com.web.baebaeBE.domain.reactioncount.entity;

import com.web.baebaeBE.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "reaction_count")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReactionCount {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "answer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
