package com.web.baebaeBE.login.application;

import com.web.baebaeBE.global.error.BusinessException;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.kakao.exception.KakaoError;
import com.web.baebaeBE.login.dao.MemberRepository;
import com.web.baebaeBE.login.domain.Member;
import com.web.baebaeBE.login.dto.MemberRequest;
import com.web.baebaeBE.login.dto.MemberResponse;
import com.web.baebaeBE.kakao.dto.KakaoUserInfoDto;
import com.web.baebaeBE.kakao.service.KakaoService;
import com.web.baebaeBE.login.exception.MemberError;
import jakarta.servlet.http.HttpServletRequest;
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
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); // 리프레시 토큰 유효기간.
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); // 액세스 토큰 유효기간.


    public MemberResponse.SignUp signUp(HttpServletRequest httpServletRequest, MemberRequest.SignUp signUpRequest) {

        KakaoUserInfoDto kakaoUserInfo = null;
        try {
            String kakaoAccessToken = httpServletRequest.getHeader("Authorization").substring(7); // "Bearer " 이후의 토큰 값만 추출
            kakaoUserInfo = kakaoService.getUserInfo(kakaoAccessToken); // accessToken을 사용하여 사용자 정보를 가져옴
        } catch(Exception e) {
            new BusinessException(KakaoError.INVALID_KAKAO_TOKEN); // Kakao 토큰이 유효하지않을 시 예외처리
        }

        // email 중복 검사
        if(memberRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail()).isPresent())
            throw new BusinessException(MemberError.DUPLICATE_MEMBER);


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
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        // 새로운 액세스 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

        return MemberResponse.AccessToken.of(member,accessToken);
    }

    public void logout(String accessToken){
        Long memberId = jwtTokenProvider.getUserId(accessToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        //현재시간으로 RefreshToken 업데이트
        member.updateTokenExpirationTime(LocalDateTime.now());
        memberRepository.save(member);
    }






}