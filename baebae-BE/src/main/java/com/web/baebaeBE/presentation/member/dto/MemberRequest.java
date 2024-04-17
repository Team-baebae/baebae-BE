package com.web.baebaeBE.presentation.member.dto;

import com.web.baebaeBE.infra.member.enums.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 시 컨트롤러에서 사용할 response, request dto 작성
 */
public class MemberRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  public static class SignUp {

    private MemberType memberType;
    private String nickname;
  }

}
