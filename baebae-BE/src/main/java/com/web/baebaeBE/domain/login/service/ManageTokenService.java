package com.web.baebaeBE.domain.login.service;

import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.login.dto.LoginResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ManageTokenService {

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); // 리프레시 토큰 유효기간.
  public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(1); // 액세스 토큰 유효기간.


  /**
   * -리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급합니다.
   * -사용자의 액세스 토큰이 만료가 되었을 때 수행됩니다.
   */
  public LoginResponse.AccessTokenResponse issueNewAccessToken(String refreshToken) {
    Member member = memberRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

    String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

    return LoginResponse.AccessTokenResponse.of(member, accessToken);
  }


  /**
   * -로그아웃 처리를 수행합니다.
   * -액세스 토큰 안에 있는 member 정보를 가져옵니다.
   * -해당 member의 refresh 토큰의 만료시간을 현재 시각으로 설정합니다.
   */
  public void logoutMember(String accessToken) {
    String memberEmail = jwtTokenProvider.getUserEmail(accessToken);

    Member member = memberRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

    member.updateTokenExpirationTime(LocalDateTime.now());
    memberRepository.save(member);
  }


}
