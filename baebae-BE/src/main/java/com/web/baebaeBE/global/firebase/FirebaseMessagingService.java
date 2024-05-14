package com.web.baebaeBE.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class FirebaseMessagingService {

    @Value("${fcm.certification}")
    private String certification;

    @Value("${fcm.projectId}")
    private String projectId;

    @Value("${fcm.baseUrl}")
    private String baseUrl;

    @Value("${fcm.messageScope}")
    private String messageScope;
    private final String BASE_URL = baseUrl;
    private final String FCM_SEND_ENDPOINT = "/v1/projects/" + projectId + "/messages:send";
    private final String MESSAGING_SCOPE = messageScope;

    private final String[] SCOPES = { MESSAGING_SCOPE };

    /**
     * Firebase를 통해 푸시 알림을 전송합니다.
     *
     * @param subscription   대상 디바이스의 FCM 토큰
     * @param title   알림 제목
     * @param body    알림 내용
     * @return        전송된 메시지의 ID
     * @throws FirebaseMessagingException FCM 전송 중 오류 발생 시
     */
    public String sendNotification(String subscription, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(new WebpushNotification(title, body))
                        .build())
                .setToken(subscription)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);

        return response;
    }


}