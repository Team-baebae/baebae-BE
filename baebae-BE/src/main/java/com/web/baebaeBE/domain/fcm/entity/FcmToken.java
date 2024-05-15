package com.web.baebaeBE.domain.fcm.entity;

import com.web.baebaeBE.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fcm_token", nullable = false)
    private String token;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime lastUsedTime;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateLastUsedTime() {
        this.lastUsedTime = LocalDateTime.now();
    }

}
