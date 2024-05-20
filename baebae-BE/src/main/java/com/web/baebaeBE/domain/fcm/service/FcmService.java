package com.web.baebaeBE.domain.fcm.service;

import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.exception.FcmException;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.exception.MemberException;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;


    public FcmToken addFcmToken(Long memberId, String fcmToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberException.NOT_EXIST_MEMBER));

        FcmToken token = FcmToken.builder()
                .token(fcmToken)
                .member(member)
                .lastUsedTime(LocalDateTime.now())
                .build();

        return fcmTokenRepository.save(token);
    }

    /*public void updateFcmToken(String oldFcmToken, String newFcmToken, Long memberId) {
        FcmToken token = fcmTokenRepository.findByToken(oldFcmToken)
                .orElseThrow(() -> new BusinessException(FcmException.NOT_FOUND_FCM));

        if (!token.getMember().getId().equals(memberId))
            throw new BusinessException(FcmException.NOT_MATCH_MEMBER);

        token.updateToken(newFcmToken);
        fcmTokenRepository.save(token);
    }*/

    public void verifyFcmToken(Long memberId, String fcmToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberException.NOT_EXIST_MEMBER));

        Optional<FcmToken> fcmTokenOptional= fcmTokenRepository.findByToken(fcmToken);
        if(fcmTokenOptional.isEmpty()) {
            FcmToken token = FcmToken.builder()
                    .token(fcmToken)
                    .member(member)
                    .lastUsedTime(LocalDateTime.now())
                    .build();

            fcmTokenRepository.save(token);
        }
    }




    // FCM 토큰의 마지막 사용 시간을 현재 시간으로 업데이트
    public void updateLastUsedTime(FcmToken fcmToken) {
        fcmToken.updateLastUsedTime();
        fcmTokenRepository.save(fcmToken);
    }

    // FCM 토큰 삭제 메서드
    public void deleteFcmToken(String fcmToken) {
        FcmToken token = fcmTokenRepository.findByToken(fcmToken)
                .orElseThrow(() -> new BusinessException(FcmException.NOT_FOUND_FCM));

        fcmTokenRepository.delete(token);
    }
}
