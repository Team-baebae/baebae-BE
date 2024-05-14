package com.web.baebaeBE.global.firebase;

import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseNotificationService {
    private final FirebaseMessagingService firebaseMessagingService;
    private final FcmTokenRepository fcmTokenRepository;


    public void notifyNewQuestion(Member member, Question question) {
        String notificationTitle = "새 질문이 도착했습니다!";
        String notificationBody = member.getNickname() + "님, 새로운 질문을 확인하세요: " + question.getContent();

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(member.getId());

        for (FcmToken fcmToken : fcmTokens)
            sendNotificationToUser(fcmToken.getToken(), notificationTitle, notificationBody);
    }
    public void notifyNewAnswer(Member member, Answer answer) {
        String notificationTitle = "새로운 답변이 도착했습니다!";
        String notificationBody = "귀하의 질문 \"" + answer.getQuestion().getContent() + "\"에 새로운 답변이 등록되었습니다.";

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(member.getId());

        for (FcmToken fcmToken : fcmTokens)
            sendNotificationToUser(fcmToken.getToken(), notificationTitle, notificationBody);
    }


    public void notifyReaction(Member member, String content, String reactionType) {
        String notificationTitle = "반응 알림!";
        String notificationBody = member.getNickname() + "님의 답변에 " + reactionType + " 반응이 있습니다: " + content;

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(member.getId());

        for (FcmToken fcmToken : fcmTokens)
            sendNotificationToUser(fcmToken.getToken(), notificationTitle, notificationBody);
    }

    private void sendNotificationToUser(String token, String title, String body) {
        String response = firebaseMessagingService.sendNotification(token, title, body);
        System.out.println("Sent message: " + response);
    }
}