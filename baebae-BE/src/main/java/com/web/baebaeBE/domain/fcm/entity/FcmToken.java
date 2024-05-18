package com.web.baebaeBE.domain.fcm.entity;

import com.web.baebaeBE.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fcm_token", nullable = false)
    private String token;

    @Column(name = "last_used_time", nullable = false)
    private LocalDateTime lastUsedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public void updateToken(String newToken) {
        this.token = newToken;
    }
    public void updateLastUsedTime() {
        this.lastUsedTime = LocalDateTime.now();
    }

}
