package com.web.baebaeBE.domain.login.service;

import com.web.baebaeBE.domain.oauth2.service.Oauth2Service;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.oauth2.dto.KakaoUserInfoDto;
import com.web.baebaeBE.domain.login.dto.LoginRequest;
import com.web.baebaeBE.domain.login.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

  private final MemberRepository memberRepository;
  private final Oauth2Service oauth2Service;
  private final JwtTokenProvider jwtTokenProvider;
  private final S3ImageStorageService s3ImageStorageService;
  public static final Duration REFRESH_TOKEN_DURATION = Duration.ofMinutes(2); // 리프레시 토큰 유효기간.
  public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(1); // 액세스 토큰 유효기간.


  /**
   * -HTTP 요청 헤더에서 카카오 Access Token 추출합니다.
   * -추출 정보를 활용해 카카오 사용자 정보를 조회합니다.
   */
  public KakaoUserInfoDto getKakaoUserInfo(HttpServletRequest httpServletRequest) {
    String kakaoAccessToken = httpServletRequest.getHeader("Authorization").substring(7);
    return oauth2Service.requestKakaoInfo(kakaoAccessToken);
  }

  /**
   * -Email을 기반으로 데이터베이스에서 기존 사용자인지 확인합니다.
   */
  public boolean isExistingUser(String email) {
    return memberRepository.existsByEmail(email);
  }

  /**
   * -Nickname 정보를 기반으로 중복되는 Nickname이 있는지 확인합니다.
   */
  public boolean isExistingNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  /**
   * -기존 회원 로그인 처리를 수행합니다.
   * -리프레시 토큰과 액세스 토큰을 새로 생성하여 업데이트
   */
  public LoginResponse.SignUpResponse loginWithExistingUser(KakaoUserInfoDto kakaoUserInfo,
                                                            LoginRequest.SignUp signUpRequest) {
    Member member = memberRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail())
        .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

    // 리프레시 토큰 및 액세스 토큰 업데이트
    String refreshToken = jwtTokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
    String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

    // 리프레시 토큰 DB 업데이트
    member.updateRefreshToken(refreshToken);
    return LoginResponse.SignUpResponse.of(memberRepository.save(member), accessToken);
  }

  /**
   * -신규 회원 가입 처리를 수행합니다.
   *
   * -새로운 Member를 DB에 저장합니다.
   *
   * -리프레시 토큰과 액세스 토큰을 새로 생성하여 업데이트
   */
  public LoginResponse.SignUpResponse signUpNewUser(KakaoUserInfoDto kakaoUserInfo,
                                                    LoginRequest.SignUp signUpRequest) {
    validateSignUpRequest(signUpRequest);

    Member newMember = Member.builder()
        .email(kakaoUserInfo.getKakaoAccount().getEmail())
        .nickname(signUpRequest.getNickname())
        .memberType(signUpRequest.getMemberType())
        .refreshToken(null)
            .profileImage(s3ImageStorageService.getDefaultFileUrl())
        .tokenExpirationTime(LocalDateTime.now().plus(REFRESH_TOKEN_DURATION))
        .build();

    // 리프레시 토큰 생성 및 DB 업데이트
    String refreshToken = jwtTokenProvider.generateToken(newMember, REFRESH_TOKEN_DURATION);
    newMember.updateRefreshToken(refreshToken);

    // 액세스 토큰 생성
    String accessToken = jwtTokenProvider.generateToken(newMember, ACCESS_TOKEN_DURATION);

    return LoginResponse.SignUpResponse.of(memberRepository.save(newMember), accessToken);
  }

  /**
   * -회원 가입 요청 정보 필수값 (회원 타입, 닉네임) 검증을 수행합니다.
   *
   * -회원 가입시에만 진행합니다. (로그인때는 X)
   */
  private void validateSignUpRequest(LoginRequest.SignUp signUpRequest) {
    if (signUpRequest.getMemberType() == null || signUpRequest.getNickname() == null) {
      throw new BusinessException(LoginException.NOT_EXIST_DATA);
    }
  }


}
