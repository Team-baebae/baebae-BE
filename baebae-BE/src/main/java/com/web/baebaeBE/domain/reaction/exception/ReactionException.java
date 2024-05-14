package com.web.baebaeBE.domain.reaction.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReactionException implements ErrorCode {

  NOT_EXIST_CONNECTION_REACTION(HttpStatus.NOT_FOUND, "R-001", "상대방이 통했당을 하지 않았습니다."),
  NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "R-002", "존재하지 않는 회원입니다."),
  NOT_EXIST_ANSWER(HttpStatus.NOT_FOUND, "R-003", "존재하지 않는 답변입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
