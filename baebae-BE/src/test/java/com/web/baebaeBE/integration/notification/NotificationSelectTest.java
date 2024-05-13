package com.web.baebaeBE.integration.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.notification.entity.Notification;
import com.web.baebaeBE.domain.notification.repository.NotificationRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class NotificationSelectTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Member testMember;
    private String accessToken;
    private String refreshToken;

    //각 테스트 전마다 가짜 유저 생성 및 토큰 발급
    @BeforeEach
    void setup() {
        testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("김예찬")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());

        accessToken = tokenProvider.generateToken(testMember, Duration.ofDays(1)); // 임시 accessToken 생성
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14)); // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);
        memberRepository.save(testMember);
    }

    //각 테스트 후마다 가짜유저 데이터 삭제
    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail(testMember.getEmail());
        if(member.isPresent())
            memberRepository.delete(member.get());
    }

    @Test
    @DisplayName("알림 리스트 조회 테스트(): 해당 유저의 알림 리스트를 조회한다.")
    void FindAllNotificationTest() throws Exception{
        // given
        NotificationRequest.create createRequest1 = new NotificationRequest.create(
                testMember.getId(),
                "배승우님이 질문을 남기셨습니다! 확인해보세요",
                "가은아! 넌 무슨색상을 좋아해?",
                NotificationRequest.EventType.NEW_QUESTION,// 이벤트 타입 설정
                null
        );
        NotificationRequest.create createRequest2 = new NotificationRequest.create(
                testMember.getId(),
                "김예찬님이 질문을 남기셨습니다! 확인해보세요",
                "가은아! 너는 무슨음식을 좋아해?",
                NotificationRequest.EventType.NEW_QUESTION,// 이벤트 타입 설정
                null
        );

        notificationService.createNotification(createRequest1);
        notificationService.createNotification(createRequest2);

        // when
        mockMvc.perform(get("/api/notifications/member/{memberId}", testMember.getId())
                        .header("Authorization", "Bearer " + accessToken))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationList").exists())
                .andExpect(jsonPath("$.notificationList[*].notificationId").exists())
                .andExpect(jsonPath("$.notificationList[*].notificationContent").exists())
                .andExpect(jsonPath("$.notificationList[*].questionContent").exists())
                .andExpect(jsonPath("$.notificationList[*].notificationTime").exists());
    }


    @Test
    @DisplayName("단일 알림 조회 테스트(): 해당 알림의 정보를 조회 한다.")
    void FindNotificationTest() throws Exception{
        // given
        NotificationRequest.create createRequest1 = new NotificationRequest.create(
                testMember.getId(),
                "배승우님이 질문을 남기셨습니다! 확인해보세요",
                "가은아! 넌 무슨색상을 좋아해?",
                NotificationRequest.EventType.NEW_QUESTION,// 이벤트 타입 설정
                null
        );
        Notification notification = notificationService.createNotification(createRequest1);

        // when
        mockMvc.perform(get("/api/notifications/{notificationId}", notification.getId())
                        .header("Authorization", "Bearer " + accessToken))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationId").exists())
                .andExpect(jsonPath("$.notificationContent").exists())
                .andExpect(jsonPath("$.questionContent").exists())
                .andExpect(jsonPath("$.notificationTime").exists());
    }
}
