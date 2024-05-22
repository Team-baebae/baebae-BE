package com.web.baebaeBE.integration.token;

import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.global.error.exception.JwtAuthenticationException;
import com.web.baebaeBE.global.jwt.JwtProperties;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Jwts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @MockBean
    private Oauth2Controller oauth2Controller;

    //각 테스트 후마다 실행
    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        if(member.isPresent())
            memberRepository.delete(member.get());
    }

    @Test
    @DisplayName("토큰 생성 테스트(): 테스트용 유저정보와 만료기간을 전달해 새로운 토큰을 생성할 수 있다.")
    void generateTokenTest() {
        // given
        Member testMember = Member.builder()
                .email("test@gmail.com")
                .nickname("테스트유저")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        testMember = memberRepository.save(testMember);

        // when
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));
        String email = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();

        // then
        assertThat(email).isEqualTo(testMember.getEmail());
    }

    @Test
    @DisplayName("토큰 검증 실패 테스트(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    void invalidTokenTest1() {
        // given
        Member testMember = Member.builder()
                .email("test@gmail.com")
                .nickname("테스트유저")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        testMember = memberRepository.save(testMember);

        String token = tokenProvider.generateToken(testMember, Duration.ofDays(-7));

        // when & then
        assertThrows(JwtAuthenticationException.class, () -> {
            tokenProvider.validToken(token);
        });
    }

    @Test
    @DisplayName("토큰 검증 실패 테스트(): 올바르지않은 토큰은 검증에 실패한다. ")
    void invalidTokenTest2() {
        // given
        String token = "eE234fG3FIe34kfg340e08wer98FJKJDodjfoije3ejr3r321dqanmj";


        // when & then
        assertThrows(JwtAuthenticationException.class, () -> {
            tokenProvider.validToken(token);
        });
    }


    @Test
    @DisplayName("토큰 검증 성공 테스트(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    void validToken_validToken() {
        // given
        Member testMember = Member.builder()
                .email("test@gmail.com")
                .nickname("테스트유저")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        testMember = memberRepository.save(testMember);

        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("토큰 인증정보 테스트(): 토큰 기반으로 인증정보를 가져올 수 있다.")
    void getAuthentication() {
        // given
        Member testMember = Member.builder()
                .email("test@gmail.com")
                .nickname("테스트유저")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        testMember = memberRepository.save(testMember);

        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername())
                .isEqualTo(testMember.getEmail());
    }
}
