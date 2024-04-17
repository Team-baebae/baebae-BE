package com.web.baebaeBE.token.service;


import com.web.baebaeBE.global.error.BusinessException;
import com.web.baebaeBE.token.client.TokenClient;
import com.web.baebaeBE.token.dto.KakaoUserInfoDto;
import com.web.baebaeBE.token.dto.TokenDto;
import com.web.baebaeBE.token.exception.TokenError;
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

    private final TokenClient tokenClient;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    public TokenDto.Response loginCallback(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        TokenDto.Request kakaoTokenRequestDto = TokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri("http://localhost:8080/oauth/kakao/callback") // 추후 수정
                .build();
        TokenDto.Response kakaoToken = tokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);

        // Kakao API를 사용하여 사용자 정보를 가져옴 -> email, nickname 정보 추가
        KakaoUserInfoDto kakaoUserInfo = getUserInfo(kakaoToken.getAccess_token());
        kakaoToken.setEmail(kakaoUserInfo.getKakaoAccount().getEmail());
        kakaoToken.setNickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickname());

        return kakaoToken;
    }


    // Acceess Token을 활용하여 Kakao API로 사용자 정보를 가져오는 메서드
    public KakaoUserInfoDto getUserInfo(String accessToken) {
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
        } catch(RestClientException e){
            log.error(String.valueOf(e));
            throw new BusinessException(TokenError.NOT_FOUND_KAKAO_INFO);
        }

        return response.getBody();
    }
}
