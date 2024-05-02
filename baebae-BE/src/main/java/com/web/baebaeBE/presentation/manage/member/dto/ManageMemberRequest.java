package com.web.baebaeBE.presentation.manage.member.dto;

import com.web.baebaeBE.infra.member.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ManageMemberRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateProfileImageDto {
    private String profileImage;
  }

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
