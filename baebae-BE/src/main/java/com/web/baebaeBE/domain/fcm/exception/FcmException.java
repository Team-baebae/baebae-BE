package com.web.baebaeBE.domain.fcm.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FcmException implements ErrorCode {

  NOT_FOUND_FCM(HttpStatus.CONFLICT, "F-001", "FCM 토큰을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
