package com.web.baebaeBE.global.firebase;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessagingService {
    private final FcmTokenRepository fcmTokenRepository;

    /**
     * Firebase를 통해 푸시 알림을 전송합니다.
     *
     * @param token   대상 디바이스의 FCM 토큰
     * @param title   알림 제목
     * @param body    알림 내용
     * @return        전송된 메시지의 ID
     * @throws FirebaseMessagingException FCM 전송 중 오류 발생 시
     */
    public String sendNotification(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            log.info("Sending FCM message to token: {}", token);
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent FCM message. Message ID: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode().equals(MessagingErrorCode.INVALID_ARGUMENT)) {
                // 토큰이 유효하지 않은 경우, 오류 코드를 반환
                return e.getMessagingErrorCode().toString();
            } else if (e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                // 재발급된 이전 토큰인 경우, 오류 코드를 반환
                return e.getMessagingErrorCode().toString();
            }
            else { // 그 외, 오류는 런타임 예외로 처리
                throw new RuntimeException(e);
            }
        }
    }
}