package com.web.baebaeBE.integration.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.notification.dto.NotificationResponse;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.notification.entity.Notification;
import com.web.baebaeBE.domain.notification.repository.NotificationRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
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
    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @MockBean
    private Oauth2Controller oauth2Controller;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Member testMember;
    private String accessToken;
    private String refreshToken;

    //각 테스트 전마다 가짜 유저 생성 및 토큰 발급
    @BeforeEach
    void setup() {
        testMember = Member.builder()  // 클래스 필드 testMember를 초기화
                .id(1L)  // ID를 명시적으로 설정
                .email("test@gmail.com")
                .nickname("test")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        when(memberRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testMember));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        accessToken = tokenProvider.generateToken(testMember, Duration.ofDays(1));  // 임시 accessToken 생성
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14));  // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);
        when(memberRepository.save(testMember)).thenReturn(testMember);

        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(testMember));
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
        Notification notification1 = Notification.builder()
                .id(1L)
                .member(testMember)
                .notificationContent("배승우님이 질문을 남기셨습니다! 확인해보세요")
                .detailContent("가은아! 넌 무슨색상을 좋아해?")
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .member(testMember)
                .notificationContent("김예찬님이 질문을 남기셨습니다! 확인해보세요")
                .detailContent("가은아! 너는 무슨음식을 좋아해?")
                .build();

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        List<NotificationResponse.NotificationContentResponse> notificationResponses = notifications.stream()
                .map(NotificationResponse.NotificationContentResponse::of)
                .collect(Collectors.toList());

        when(notificationService.getNotificationsListByMember(testMember.getId()))
                .thenReturn(new NotificationResponse.NotificationListResponse(notificationResponses));

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

}

