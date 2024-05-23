package com.web.baebaeBE.domain.login.dto;

import com.web.baebaeBE.domain.member.entity.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;


public class LoginRequest {

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

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CheckNickname{
    @Schema(example = "김예찬")
    private String nickname;
  }

}
