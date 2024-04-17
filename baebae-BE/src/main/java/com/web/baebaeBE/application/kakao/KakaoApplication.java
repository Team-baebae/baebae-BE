package com.web.baebaeBE.application.kakao;


import com.web.baebaeBE.domain.kakao.service.TokenService;
import com.web.baebaeBE.presentation.kakao.dto.KakaoDto;
import com.web.baebaeBE.presentation.kakao.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KakaoApplication {

  TokenService tokenService;


  public KakaoDto.Response loginCallback(String code) {
    KakaoDto.Response kakaoToken = tokenService.requestKakaoToken(code);

    KakaoUserInfoDto kakaoUserInfo = tokenService.requestKakaoInfo(kakaoToken.getAccess_token());

    kakaoToken = tokenService.addUserInfo(kakaoToken, kakaoUserInfo);

    return kakaoToken;
  }


}
