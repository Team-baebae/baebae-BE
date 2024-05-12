package com.web.baebaeBE.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class MemberRequest {



  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateFcmTokenDto {
    private String fcmToken;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateNicknameDto {
    private String nickname;
  }

}
