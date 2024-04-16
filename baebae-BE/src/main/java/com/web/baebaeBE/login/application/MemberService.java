package com.web.baebaeBE.login.application;

import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.login.dao.MemberRepository;
import com.web.baebaeBE.login.domain.Member;
import com.web.baebaeBE.login.dto.MemberRequest;
import com.web.baebaeBE.login.dto.MemberResponse;
import com.web.baebaeBE.token.dto.KakaoUserInfoDto;
import com.web.baebaeBE.token.service.TokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); // 리프레시 토큰 유효기간.
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); // 액세스 토큰 유효기간.


    public MemberResponse.SignUp signUp(String KakaoAccessToken, MemberRequest.SignUp signUpRequest) {

        // accessToken을 사용하여 사용자 정보를 가져옴
        KakaoUserInfoDto kakaoUserInfo = tokenService.getUserInfo(KakaoAccessToken);


        // email 중복 검사
        /*memberRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail())
                .orElseThrow(() -> new DuplicateEmailException("이미 존재하는 이메일입니다.")); // 추후 수정*/

        // Member 객체 생성
        Member member = memberRepository.save(Member.builder()
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .nickname(signUpRequest.getNickname())
                .memberType(signUpRequest.getMemberType())
                .refreshToken(null)
                .tokenExpirationTime(LocalDateTime.now().plus(REFRESH_TOKEN_DURATION))
                .build()
        );

        // 리프레시 토큰 생성 및 DB 업데이트
        String refreshToken = jwtTokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        member.updateRefreshToken(refreshToken);

        // 액세스 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);


        return MemberResponse.SignUp.of(member, accessToken);
    }


    // 새로운 AccessToken 발급
    public MemberResponse.AccessToken newAccessToken(String refreshToken){
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다.")); // 추후 수정*/

        // 새로운 액세스 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

        return MemberResponse.AccessToken.of(member,accessToken);
    }

    public void logout(String accessToken){
        System.out.println(accessToken);
        Long memberId = jwtTokenProvider.getUserId(accessToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다.")); // 추후 수정*/

        //현재시간으로 RefreshToken 업데이트
        member.updateTokenExpirationTime(LocalDateTime.now());
        memberRepository.save(member);
    }






}