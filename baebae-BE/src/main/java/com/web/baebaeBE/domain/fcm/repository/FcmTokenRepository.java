package com.web.baebaeBE.domain.fcm.repository;

import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByMemberId(Long memberId);
}
