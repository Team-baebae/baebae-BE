package com.web.baebaeBE.domain.fcm.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FcmException implements ErrorCode {

  NOT_FOUND_FCM(HttpStatus.CONFLICT, "F-001", "FCM 토큰을 찾을 수 없습니다."),
  NOT_MATCH_MEMBER(HttpStatus.CONFLICT, "F-002", "FCM 토큰과 회원 정보가 일치하지 않습니다."),
  TOKEN_ALREADY_EXISTS(HttpStatus.CONFLICT, "F-003", "이미 등록된 FCM 토큰입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
