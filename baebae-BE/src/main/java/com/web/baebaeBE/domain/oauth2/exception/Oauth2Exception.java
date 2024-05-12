package com.web.baebaeBE.domain.oauth2.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Oauth2Exception implements ErrorCode {

  INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "K-001", "유효하지않은 KaKao Token 입니다."),
  INVALID_CODE_OR_URL(HttpStatus.UNAUTHORIZED, "K-002", "인증에 실패했습니다. 승인코드와 Redirect Uri를 다시 확인해주세요.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
