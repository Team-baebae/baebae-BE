package com.web.baebaeBE.domain.member.service;

import com.web.baebaeBE.domain.kakao.service.TokenService;
import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.BusinessException;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.presentation.kakao.dto.KakaoUserInfoDto;
import com.web.baebaeBE.presentation.member.dto.MemberRequest;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

  private final MemberRepository memberRepository;
  private final TokenService tokenService;
  private final JwtTokenProvider jwtTokenProvider;
  public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); // 리프레시 토큰 유효기간.
  public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); // 액세스 토큰 유효기간.


  /**
   * -HTTP 요청 헤더에서 카카오 Access Token 추출합니다.
   * -추출 정보를 활용해 카카오 사용자 정보를 조회합니다.
   */
  public KakaoUserInfoDto getKakaoUserInfo(HttpServletRequest httpServletRequest) {
    String kakaoAccessToken = httpServletRequest.getHeader("Authorization").substring(7);
    return tokenService.requestKakaoInfo(kakaoAccessToken);
  }

  /**
   * -Email을 기반으로 데이터베이스에서 기존 사용자인지 확인합니다.
   */
  public boolean isExistingUser(String email) {
    return memberRepository.existsByEmail(email);
  }

  /**
   * -기존 회원 로그인 처리를 수행합니다.
   * -리프레시 토큰과 액세스 토큰을 새로 생성하여 업데이트
   */
  public MemberResponse.SignUp loginWithExistingUser(KakaoUserInfoDto kakaoUserInfo,
      MemberRequest.SignUp signUpRequest) {
    Member member = memberRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail())
        .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

    // 리프레시 토큰 및 액세스 토큰 업데이트
    String refreshToken = jwtTokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
    String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

    // 리프레시 토큰 DB 업데이트
    member.updateRefreshToken(refreshToken);
    return MemberResponse.SignUp.of(memberRepository.save(member), accessToken);
  }

  /**
   * -신규 회원 가입 처리를 수행합니다.
   *
   * -새로운 Member를 DB에 저장합니다.
   *
   * -리프레시 토큰과 액세스 토큰을 새로 생성하여 업데이트
   */
  public MemberResponse.SignUp signUpNewUser(KakaoUserInfoDto kakaoUserInfo,
      MemberRequest.SignUp signUpRequest) {
    validateSignUpRequest(signUpRequest);

    Member newMember = Member.builder()
        .email(kakaoUserInfo.getKakaoAccount().getEmail())
        .nickname(signUpRequest.getNickname())
        .memberType(signUpRequest.getMemberType())
        .refreshToken(null)
        .tokenExpirationTime(LocalDateTime.now().plus(REFRESH_TOKEN_DURATION))
        .build();

    // 리프레시 토큰 생성 및 DB 업데이트
    String refreshToken = jwtTokenProvider.generateToken(newMember, REFRESH_TOKEN_DURATION);
    newMember.updateRefreshToken(refreshToken);

    // 액세스 토큰 생성
    String accessToken = jwtTokenProvider.generateToken(newMember, ACCESS_TOKEN_DURATION);

    return MemberResponse.SignUp.of(memberRepository.save(newMember), accessToken);
  }

  /**
   * -회원 가입 요청 정보 필수값 (회원 타입, 닉네임) 검증을 수행합니다.
   *
   * -회원 가입시에만 진행합니다. (로그인때는 X)
   */
  private void validateSignUpRequest(MemberRequest.SignUp signUpRequest) {
    if (signUpRequest.getMemberType() == null || signUpRequest.getNickname() == null) {
      throw new BusinessException(MemberError.NOT_EXIST_DATA);
    }
  }


}