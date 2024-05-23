package com.web.baebaeBE.integration.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import com.web.baebaeBE.domain.login.dto.LoginResponse;
import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.domain.oauth2.service.Oauth2Service;
import com.web.baebaeBE.domain.login.service.LoginService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.oauth2.dto.KakaoUserInfoDto;
import com.web.baebaeBE.domain.login.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private Oauth2Service oauth2Service;
    @MockBean
    private LoginService loginService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private FcmTokenRepository fcmTokenRepository;
    @MockBean
    private Oauth2Controller oauth2Controller;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private String refreshToken;

    //각 테스트 전마다 실행
    @BeforeEach
    void setup() {
        Member testMember = Member.builder()
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

        // FcmToken 생성 및 Member에 연결
        FcmToken testFcmToken = FcmToken.builder()
                .token("testFcmToken")
                .member(testMember)
                .lastUsedTime(LocalDateTime.now())
                .build();
        when(fcmTokenRepository.save(any(FcmToken.class))).thenReturn(testFcmToken);

        //fcm 토큰 찾기
        when(fcmTokenRepository.findByToken("testFcmToken")).thenReturn(Optional.of(testFcmToken));
    }

    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(value -> memberRepository.delete(value));
    }

        @Test
        @DisplayName("oAuth2 기반 로그인 테스트(): 가짜 카카오 토큰을 통해 로그인 및 회원가입 테스트한다.")
        public void testOauthSignUp() throws Exception {
            // given
            KakaoUserInfoDto.KakaoAccount.Profile profile = new KakaoUserInfoDto.KakaoAccount.Profile("김예찬");
            KakaoUserInfoDto.KakaoAccount kakaoAccount = new KakaoUserInfoDto.KakaoAccount();
            kakaoAccount.setEmail("tioon74@gmail.com");
            kakaoAccount.setProfile(profile);

            KakaoUserInfoDto kakaoUserInfo = new KakaoUserInfoDto("3", kakaoAccount);

            LoginResponse.SignUpResponse signUpResponse = new LoginResponse.SignUpResponse();
            signUpResponse.setId(1L);
            signUpResponse.setEmail("tioon74@gmail.com");
            signUpResponse.setNickname("김예찬");
            signUpResponse.setMemberType(MemberType.KAKAO);
            signUpResponse.setAccessToken("newAccessToken");
            signUpResponse.setRefreshToken("newRefreshToken");

            when(loginService.getKakaoUserInfo(any(HttpServletRequest.class))).thenReturn(kakaoUserInfo);
            when(oauth2Service.requestKakaoInfo(anyString())).thenReturn(kakaoUserInfo);
            when(loginService.loginWithExistingUser(any(KakaoUserInfoDto.class), any(LoginRequest.SignUp.class))).thenReturn(signUpResponse);
            when(loginService.signUpNewUser(any(KakaoUserInfoDto.class), any(LoginRequest.SignUp.class))).thenReturn(signUpResponse);

            LoginRequest.SignUp signUpRequest = new LoginRequest.SignUp(MemberType.KAKAO, "김예찬");

            // when
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + refreshToken)
                            .content(objectMapper.writeValueAsString(signUpRequest)))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is("tioon74@gmail.com")))
                    .andExpect(jsonPath("$.nickname", is("김예찬")))
                    .andExpect(jsonPath("$.memberType", is("KAKAO")))
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()));

    }

    @Test
    @DisplayName("토큰 재발급 테스트(): 테스트용 RefreshToken을 전달해 새로운 AccessToken을 생성한다.")
    public void testRefreshToken() throws Exception {
        // given

        // when
        mockMvc.perform(post("/api/auth/token/refresh")
                        .header("Authorization", "Bearer " + refreshToken))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.nickname").exists())
                .andExpect(jsonPath("$.memberType").exists())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    // 로그아웃 테스트
    @Test
    @DisplayName("로그아웃 테스트(): 테스트용 AccessToken을 전달해 사용자 로그아웃을 시킨다.")
    public void testLogout() throws Exception {
        // given
        String fcmToken = "testFcmToken";

        // when
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("fcmToken", fcmToken))
        // then
                .andExpect(status().isOk());
    }
}
