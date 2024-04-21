package com.web.baebaeBE.presentation.member.dto;

import com.web.baebaeBE.infra.member.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class MemberRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignUp{

    @Schema(example = "KAKAO")
    private MemberType memberType;
    @Schema(example = "김예찬")
    private String nickname;
  }

}
