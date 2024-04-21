package com.web.baebaeBE.integration.token;

import com.web.baebaeBE.config.jwt.JwtFactory;
import com.web.baebaeBE.global.error.exception.JwtAuthenticationException;
import com.web.baebaeBE.global.jwt.JwtProperties;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.enums.MemberType;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MemberRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("토큰 생성 테스트(): 테스트용 유저정보와 만료기간을 전달해 새로운 토큰을 생성할 수 있다.")
    void generateTokenTest() {
        // given
        Member testMember = userRepository.save(Member.builder()
                .email("user@gmail.com")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());

        // when
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));
        Long userId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId());

        // then
        assertThat(userId).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("토큰 검증 실패 테스트(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    void invalidTokenTest1() {
        // given
        Member testMember = userRepository.save(Member.builder()
                .email("user@gmail.com")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());
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
        Member testMember = userRepository.save(Member.builder()
                .email("user@gmail.com")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());
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
        Member testMember = userRepository.save(Member.builder()
                .email("user@gmail.com")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername())
                .isEqualTo(testMember.getEmail());
    }
}
