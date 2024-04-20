package com.web.baebaeBE.presentation.member;


import com.web.baebaeBE.application.member.MemberApplication;
import com.web.baebaeBE.presentation.member.api.MemberApi;
import com.web.baebaeBE.presentation.member.dto.MemberRequest;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 클라이언트로부터 소셜 로그인을 받는 컨트롤러 localhost:8080/api/oauth/login { Body : "memberType" : "KAKAO"만 있고
 * Authorization 안붙이는 경우 -> "Authorization Member가 빈값입니다" Authrorization과 같이 보내는데 앞에 Bearer 안붙이는 경우
 * -> "인증 타입이 Bearer 타입이 아닙니다"
 * <p>
 * grantType, accessToken, accessTokenExpireTime, refreshToken, refreshTokenExpireTime 반환
 * <p>
 * }
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class MemberController implements MemberApi {


  private final MemberApplication memberApplication;

  //로그인
  @PostMapping("/login")
  public ResponseEntity<MemberResponse.SignUpResponse> oauthSignUp(
      @RequestBody MemberRequest.SignUp signUpRequest,
      HttpServletRequest httpServletRequest
  ) {
    return ResponseEntity.ok(memberApplication.login(httpServletRequest, signUpRequest));
  }


  // Access Token 재발급
  @GetMapping("/token/refresh")
  public ResponseEntity<MemberResponse.AccessTokenResponse> refreshToken(
      HttpServletRequest httpServletRequest
  ) {
    // Refresh Token 검증 및 추출
    String authorizationHeader = httpServletRequest.getHeader("Authorization");
    String refreshToken = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      refreshToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
    }

    // Access Token 재발급
    return ResponseEntity.ok(memberApplication.newAccessToken(refreshToken));
  }

  //로그아웃
  @GetMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
    String authorizationHeader = httpServletRequest.getHeader("Authorization");
    String accessToken = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      accessToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
    }

    //로그아웃 진행
    memberApplication.logout(accessToken);

    return ResponseEntity.ok().build();
  }
}
