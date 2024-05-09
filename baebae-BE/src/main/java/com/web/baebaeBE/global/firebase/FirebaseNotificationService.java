package com.web.baebaeBE.global.firebase;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseNotificationService {
    private final FirebaseMessagingService firebaseMessagingService;

    @Autowired
    public FirebaseNotificationService(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;
    }

    public void notifyNewQuestion(Member member, Question question) {
        String notificationTitle = "새 질문이 도착했습니다!";
        String notificationBody = member.getNickname() + "님, 새로운 질문을 확인하세요: " + question.getContent();
        try {
            String response = firebaseMessagingService.sendNotification(member.getFcmToken(), notificationTitle, notificationBody);
            System.out.println("Sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyReaction(Member member, String content, String reactionType) {
        String notificationTitle = "반응 알림!";
        String notificationBody = member.getNickname() + "님의 답변에 " + reactionType + " 반응이 있습니다: " + content;
        try {
            String response = firebaseMessagingService.sendNotification(member.getFcmToken(), notificationTitle, notificationBody);
            System.out.println("Sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}