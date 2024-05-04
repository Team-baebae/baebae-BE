package com.web.baebaeBE.presentation.member;


import com.web.baebaeBE.application.member.MemberApplication;
import com.web.baebaeBE.presentation.member.api.MemberApi;
import com.web.baebaeBE.presentation.member.dto.MemberRequest;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

  //회원가입 유무 판별
  @GetMapping("/isExisting")
  public ResponseEntity<MemberResponse.isExistingUserResponse> isExistingUser(
          HttpServletRequest httpServletRequest
  ) {
    return ResponseEntity.ok(memberApplication.checkIsExisting(httpServletRequest));
  }

  //닉네임 중복 유무 확인
  @GetMapping("/nickname/isExisting")
  public ResponseEntity<MemberResponse.isExistingUserResponse> isExistingNickname(
          @RequestParam String nickname
  ) {
    return ResponseEntity.ok(memberApplication.checkNicknameIsExisting(nickname));
  }

  // Access Token 재발급
  @PostMapping("/token/refresh")
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
  @PostMapping("/logout")
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
