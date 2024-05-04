package com.web.baebaeBE.domain.manage.member.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ManageMemberError implements ErrorCode {
  NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "MM-001", "존재하지 않는 회원입니다."),
  NOT_VERIFY_MEMBET_WITH_TOKEN(HttpStatus.UNAUTHORIZED,"MM-002", "회원정보와 토큰정보가 일치하지 않습니다.");


  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
