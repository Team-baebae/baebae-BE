package com.web.baebaeBE.domain.kakao.service;

import com.web.baebaeBE.domain.kakao.exception.KakaoError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.presentation.kakao.dto.KakaoDto;
import com.web.baebaeBE.presentation.kakao.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

  private final KakaoClient kakaoClient;
  private final RestTemplate restTemplate;

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String clientId;
  @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
  private String clientSecret;

  /**
   * -카카오 로그인 인가 코드를 이용하여 카카오 토큰을 발급받습니다.
   * -카카오 로그인 요청 정보 생성 -카카오 서버에 POST 요청 전송
   * -카카오 토큰 응답 정보 객체 반환
   */
  public KakaoDto.Response requestKakaoToken(String code) {
    String contentType = "application/x-www-form-urlencoded;charset=utf-8";
    KakaoDto.Request kakaoTokenRequestDto = KakaoDto.Request.builder()
        .client_id(clientId)
        .client_secret(clientSecret)
        .grant_type("authorization_code")
        .code(code)
        .redirect_uri("http://localhost:8080/oauth/kakao/callback") // 추후 수정
        .build();
    return kakaoClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
  }

  /**
   * -카카오 토큰 정보에 사용자 이메일과 닉네임을 추가합니다.
   */
  public KakaoDto.Response addUserInfo(KakaoDto.Response kakaoToken,
      KakaoUserInfoDto kakaoUserInfo) {
    kakaoToken.setEmail(kakaoUserInfo.getKakaoAccount().getEmail());
    kakaoToken.setNickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickname());
    return kakaoToken;
  }

  /**
   * -카카오 액세스 토큰을 이용하여 카카오 사용자 정보를 조회합니다.
   * -인증 헤더에 액세스 토큰 설정
   * -카카오 사용자 정보 조회 API 호출
   * -카카오 사용자 정보 응답 객체
   * 반환
   */
  public KakaoUserInfoDto requestKakaoInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<KakaoUserInfoDto> response;

    try {
      response = restTemplate.exchange(
          "https://kapi.kakao.com/v2/user/me",
          HttpMethod.GET,
          entity,
          KakaoUserInfoDto.class
      );
    } catch (RestClientException e) {
      log.error(String.valueOf(e));
      throw new BusinessException(KakaoError.INVALID_KAKAO_TOKEN);
    }

    return response.getBody();
  }

}
