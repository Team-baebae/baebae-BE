package com.web.baebaeBE.domain.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {

  private String id;
  //회원 번호
  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  public KakaoUserInfoDto(String id, String email, String nickname) {
    this.id = id;
    this.kakaoAccount = new KakaoAccount(email, new KakaoAccount.Profile(nickname));
  }

  //카카오 계정 정보
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class KakaoAccount {

    private String email;
    // 카카오 계정 대표 이메일
    private Profile profile;

    // 프로필 정보
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profile {

      private String nickname;
      //닉네임

    }

  }
}
