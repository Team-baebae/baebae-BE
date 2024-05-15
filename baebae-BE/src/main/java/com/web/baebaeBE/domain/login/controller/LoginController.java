package com.web.baebaeBE.domain.login.controller;


import com.web.baebaeBE.domain.fcm.service.FcmService;
import com.web.baebaeBE.domain.login.controller.api.LoginApi;
import com.web.baebaeBE.domain.login.dto.LoginRequest;
import com.web.baebaeBE.domain.login.dto.LoginResponse;
import com.web.baebaeBE.domain.login.service.LoginService;
import com.web.baebaeBE.domain.login.service.ManageTokenService;
import com.web.baebaeBE.domain.oauth2.dto.KakaoUserInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController implements LoginApi {


  private final LoginService loginService;
  private final ManageTokenService manageTokenService;
  private final FcmService fcmService;

  //로그인
  @PostMapping("/login")
  public ResponseEntity<LoginResponse.SignUpResponse> oauthSignUp(
      @RequestBody LoginRequest.SignUp signUpRequest,
      HttpServletRequest httpServletRequest
  ) {
    //카카오 토큰으로 정보 가져옴
    KakaoUserInfoDto kakaoUserInfo = loginService.getKakaoUserInfo(httpServletRequest);

    // 이미 회원이 존재하면 로그인, 없을 시 회원가입 진행
    LoginResponse.SignUpResponse response = null;
    if (loginService.isExistingUser(kakaoUserInfo.getKakaoAccount().getEmail()))
      response = loginService.loginWithExistingUser(kakaoUserInfo, signUpRequest);
    else
      response = loginService.signUpNewUser(kakaoUserInfo, signUpRequest);

    if(signUpRequest.getFcmToken() != null)
      fcmService.verifyFcmToken(response.getId(), signUpRequest.getFcmToken());

    return ResponseEntity.ok(response);
  }

  //회원가입 유무 판별
  @GetMapping("/isExisting")
  public ResponseEntity<LoginResponse.isExistingUserResponse> isExistingUser(
          HttpServletRequest httpServletRequest
  ) {
    //카카오 토큰으로 정보 가져옴
    KakaoUserInfoDto kakaoUserInfo = loginService.getKakaoUserInfo(httpServletRequest);

    //이미 회원이 존재하면 true, 아니면 false
    if(loginService.isExistingUser(kakaoUserInfo.getKakaoAccount().getEmail()))
      return ResponseEntity.ok(new LoginResponse.isExistingUserResponse(true));
    else
      return ResponseEntity.ok(new LoginResponse.isExistingUserResponse(false));
  }

  //닉네임 중복 유무 확인
  @GetMapping("/nickname/isExisting")
  public ResponseEntity<LoginResponse.isExistingUserResponse> isExistingNickname(
          @RequestParam String nickname
  ) {
    // 이미 회원이 존재하면 true, 아니면 false
    if(loginService.isExistingNickname(nickname))
      return ResponseEntity.ok(new LoginResponse.isExistingUserResponse(true));
    else
      return ResponseEntity.ok(new LoginResponse.isExistingUserResponse(false));
  }

  // Access Token 재발급
  @PostMapping("/token/refresh")
  public ResponseEntity<LoginResponse.AccessTokenResponse> refreshToken(
      HttpServletRequest httpServletRequest
  ) {
    // Refresh Token 검증 및 추출
    String authorizationHeader = httpServletRequest.getHeader("Authorization");
    String refreshToken = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      refreshToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
    }

    // Access Token 재발급
    return ResponseEntity.ok(manageTokenService.issueNewAccessToken(refreshToken));
  }

  //로그아웃
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
          HttpServletRequest httpServletRequest,
          @RequestParam(required = false) String fcmToken
  ) {
    String authorizationHeader = httpServletRequest.getHeader("Authorization");
    String accessToken = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      accessToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
    }

    //로그아웃 진행
    manageTokenService.logoutMember(accessToken);

    //fcmToken 삭제
    if(fcmToken != null)
      fcmService.deleteFcmToken(fcmToken);

    return ResponseEntity.ok().build();
  }

}
