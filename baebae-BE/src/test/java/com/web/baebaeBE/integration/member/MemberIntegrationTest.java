package com.web.baebaeBE.integration.member;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.oauth2.service.Oauth2Service;
import com.web.baebaeBE.domain.login.service.LoginService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.oauth2.dto.KakaoUserInfoDto;
import com.web.baebaeBE.domain.login.dto.LoginRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
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
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private MemberRepository memberRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private String refreshToken;

    //각 테스트 전마다 실행
    @BeforeEach
    void setup() {
        Member testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("test")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());

        accessToken = tokenProvider.generateToken(testMember, Duration.ofDays(1)); // 임시 accessToken 생성
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14)); // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);
        memberRepository.save(testMember);
    }

    //각 테스트 후마다 실행
    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        if(member.isPresent())
            memberRepository.delete(member.get());
    }


    @Test
    @DisplayName("oAuth2 기반 로그인 테스트(): 가짜 카카오 토큰을 통해 로그인 및 회원가입 테스트한다.")
    public void testOauthSignUp() throws Exception {
        // given
        // 가짜 카카오 토큰 Mock 객체 설정
        KakaoUserInfoDto kakaoUserInfo = new KakaoUserInfoDto("3","tioon74@gmail.com", "김예찬");
        Mockito.when(oauth2Service.requestKakaoInfo(any(String.class))).thenReturn(kakaoUserInfo); // 카카오 토큰 인증 메서드 Mock 설정

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(
                Mockito.eq("https://kapi.kakao.com/v2/user/me"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(KakaoUserInfoDto.class)
        )).thenReturn(new ResponseEntity<>(kakaoUserInfo, HttpStatus.OK));

        // HttpRequest Body 설정
        LoginRequest.SignUp signUpRequest
                = new LoginRequest.SignUp(MemberType.KAKAO, "김예찬"); // 요청


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

        // when
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
        // then
                .andExpect(status().isOk());
    }
}