package com.web.baebaeBE.global.scheduler;

import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import com.web.baebaeBE.domain.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component // 스프링 빈으로 등록
@RequiredArgsConstructor // final 필드나 @NonNull 필드에 대한 생성자를 자동으로 생성
public class FcmTokenSchduler {
    private final FcmTokenRepository fcmTokenRepository; // FcmTokenRepository 주입

    // 매일 자정에 실행되는 스케줄러
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnusedTokens() {
        // 한 달 전 시간 계산
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // 모든 FcmToken 조회
        List<FcmToken> tokens = fcmTokenRepository.findAll();
        for (FcmToken token : tokens) {
            // 마지막 사용 시간이 한 달 이상 지난 토큰 삭제
            if (token.getLastUsedTime().isBefore(oneMonthAgo)) {
                fcmTokenRepository.delete(token);
            }
        }
    }
}
