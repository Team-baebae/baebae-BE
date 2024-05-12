package com.web.baebaeBE.domain.oauth2.service;

import com.web.baebaeBE.domain.oauth2.dto.KakaoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign Cleint는 웹 서비스 클라이언트를 보다 쉽게 작성할 수 있도록 도와줌 interface(KakaoTokenClient) 를 작성하고 annotation을
 * 붙여주면 세부적인 내용 없이 사용할 수 있음 코드 복잡도 감소
 * <p>
 * 카카오 개발자 센터 - 카카오 로그인 REST API - 토큰 받기 참조 POST /oauth/token HTTP/1.1 Host: kauth.kakao.com
 * Content-type: application/x-www-form-urlencoded;charset=utf-8
 * <p>
 * 카카오 로그인 flow에서 사용
 */

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoClient {

  @PostMapping(value = "/oauth/token", consumes = "application/json")
  KakaoDto.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
      @SpringQueryMap KakaoDto.Request request
  );

}
