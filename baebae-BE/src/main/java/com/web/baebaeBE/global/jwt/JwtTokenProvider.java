package com.web.baebaeBE.global.jwt;

import com.web.baebaeBE.login.domain.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;


@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    private final JwtProperties jwtProperties; // jwt 설정값 클래스


    //토큰 생성 메서드
    public String generateToken(Member member, Duration expiredAt) { // User , 만료시간을 입력받음.
        Date now = new Date(); // 현재시간을 가져옴.
        Date expiry = new Date(now.getTime() + expiredAt.toMillis()); // 만료시간을 계산함.
        return makeToken(expiry, member); // 만료시간 설정후 토큰 생성
    }


    //토큰 제작 메서드
    private String makeToken(Date expiry, Member member) {
        Date now = new Date();

        System.out.println(member.getId());


        //jwt 빌더
        return Jwts.builder()
                // Header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)

                // Payload
                .setIssuer(jwtProperties.getIssuer()) // 토큰 발행자
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(expiry) // 토큰 만료 시간
                .setSubject(member.getEmail()) //토큰 주제
                .setId(String.valueOf(member.getId())) // member Id

                // Signature
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명 알고리즘 및 키 설정
                .compact();
    }

    //토큰 유효성 검사 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //토큰에서 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰에서 클레임 가져옴.
        //클레인 = 토큰의 식별자, 권한, 만료시간이 포함.
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("USER")); // 토큰 권한설정

        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuer());
        System.out.println(claims.getId());
        System.out.println(claims.getExpiration());


        // 인증 객체 생성 후 반환
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }


    //토큰에서 사용자 ID 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token); // 토큰에서 클레임 가져옴.
        return claims.get("id", Long.class); // 사용자 ID반환
    }

    //토큰을 파싱하여 클레임 가져오는 메서드
     Claims getClaims(String token) {
        return Jwts.parser() // jwt 파서
                .setSigningKey(jwtProperties.getSecretKey()) // 서명키 설정
                .parseClaimsJws(token) // 토큰 파싱
                .getBody(); // 클레임 반환
    }
}
