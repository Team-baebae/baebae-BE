package com.web.baebaeBE.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {
    /**
     * Firebase를 통해 푸시 알림을 전송합니다.
     *
     * @param token   대상 디바이스의 FCM 토큰
     * @param title   알림 제목
     * @param body    알림 내용
     * @return        전송된 메시지의 ID
     * @throws FirebaseMessagingException FCM 전송 중 오류 발생 시
     */
    public String sendNotification(String token, String title, String body) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        // FirebaseMessaging 인스턴스를 통해 메시지 전송
        return FirebaseMessaging.getInstance().send(message);
    }
}