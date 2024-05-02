package com.web.baebaeBE.application.member;

import com.web.baebaeBE.domain.member.service.LoginService;
import com.web.baebaeBE.domain.member.service.ManageTokenService;
import com.web.baebaeBE.presentation.kakao.dto.KakaoUserInfoDto;
import com.web.baebaeBE.presentation.member.dto.MemberRequest;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberApplication {

  private final LoginService loginService;
  private final ManageTokenService manageTokenService;

  public MemberResponse.SignUpResponse login(HttpServletRequest httpServletRequest,
      MemberRequest.SignUp signUpRequest) {
    //카카오 토큰으로 정보 가져옴
    KakaoUserInfoDto kakaoUserInfo = loginService.getKakaoUserInfo(httpServletRequest);

    // 이미 회원이 존재하면 로그인, 없을 시 회원가입 진행
    if (loginService.isExistingUser(kakaoUserInfo.getKakaoAccount().getEmail())) {
      return loginService.loginWithExistingUser(kakaoUserInfo, signUpRequest);
    } else {
      return loginService.signUpNewUser(kakaoUserInfo, signUpRequest);
    }
  }

  public MemberResponse.isExistingUserResponse checkIsExisting(HttpServletRequest httpServletRequest){
    //카카오 토큰으로 정보 가져옴
    KakaoUserInfoDto kakaoUserInfo = loginService.getKakaoUserInfo(httpServletRequest);

    //이미 회원이 존재하면 true, 아니면 false
    if(loginService.isExistingUser(kakaoUserInfo.getKakaoAccount().getEmail()))
      return new MemberResponse.isExistingUserResponse(true);
    else
      return new MemberResponse.isExistingUserResponse(false);
  }


  public MemberResponse.AccessTokenResponse newAccessToken(String refreshToken) {
    return manageTokenService.issueNewAccessToken(refreshToken); // 함수 호출
  }


  public void logout(String accessToken) {
    manageTokenService.logoutMember(accessToken); // 함수 호출
  }


}