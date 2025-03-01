package com.web.baebaeBE.domain.Follow.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FollowException implements ErrorCode {

  NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "FL-001", "존재하지 않는 회원입니다."),
  NOT_EXIST_FOLLOW(HttpStatus.NOT_FOUND, "FL-002", "존재하지 않는 팔로우 관계 입니다."),
  ALREADY_EXISTS_FOLLOW(HttpStatus.CONFLICT, "FL-003", "이미 존재하는 팔로우 관계입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
