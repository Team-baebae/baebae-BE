package com.web.baebaeBE.domain.kakao.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KakaoError implements ErrorCode {

  INVALID_KAKAO_TOKEN(HttpStatus.NOT_FOUND, "K-001", "유효하지않은 KaKao Token 입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
