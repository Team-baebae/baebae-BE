package com.web.baebaeBE.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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

    private static final String PROJECT_ID = "baebae-ff525";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    private static final String TITLE = "FCM Notification";
    private static final String BODY = "Notification from FCM";
    public static final String MESSAGE_KEY = "message";
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
    private static String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream("baebae-ff525-firebase-adminsdk-zbc8h-7fd10e518b.json"))
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshAccessToken();
        return googleCredentials.getAccessToken().getTokenValue();
     }

//    private static HttpURLConnection getConnection() throws IOException {
//        // [START use_access_token]
//        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
//        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
//        return httpURLConnection;
//        // [END use_access_token]
//    }
}