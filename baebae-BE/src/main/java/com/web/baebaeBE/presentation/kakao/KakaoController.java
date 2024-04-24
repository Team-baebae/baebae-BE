package com.web.baebaeBE.presentation.kakao;

import com.web.baebaeBE.application.kakao.KakaoApplication;
import com.web.baebaeBE.presentation.kakao.api.KakaoApi;
import com.web.baebaeBE.presentation.kakao.dto.KakaoDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping()
public class KakaoController implements KakaoApi {

  private final KakaoApplication kakaoApplication;

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String clientId;
  @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
  private String clientSecret;
  @Value("${spring.profiles.active}")
  private String version;



  @GetMapping("/api/oauth/kakao")
  public String login() {
    if(version.equals("local")) {
      return "oauthLogin-local"; // 로컬용 oauth2 로그인
    } else {
      return "oauthLogin-deploy"; // 배포용 oauth2 로그인
    }
  }


  @GetMapping("/oauth/kakao/callback")
  public ResponseEntity<KakaoDto.Response> loginCallback(
          @RequestParam("code") String code,
          @RequestParam(value = "redirectUri", required = false) String redirectUri) { // 프론트만 필수, 로컬테스트는 필수 x
    KakaoDto.Response kakaoToken = kakaoApplication.loginCallback(code, redirectUri);
    return ResponseEntity.ok(kakaoToken);
  }


  @Operation(summary = "백엔드용 TEST API입니다.")
  @GetMapping("api/test")
  @ResponseBody
  public void test(HttpServletRequest request) {
    //System.out.println(request.getAttribute("id"));
    return;
  }

}
