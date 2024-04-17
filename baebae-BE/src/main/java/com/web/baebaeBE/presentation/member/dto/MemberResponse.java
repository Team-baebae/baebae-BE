package com.web.baebaeBE.presentation.member.dto;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 시 컨트롤러에서 사용할 response, request dto 작성
 */
public class MemberResponse {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignUp {

    private Long id;
    private String email;
    private String nickname;
    private MemberType memberType;
    private String accessToken;
    private String refreshToken;


    public static SignUp of(Member member, String accessToken) {
      return SignUp.builder()
          .id(member.getId())
          .email(member.getEmail())
          .nickname(member.getNickname())
          .memberType(member.getMemberType())
          .refreshToken(member.getRefreshToken())
          .accessToken(accessToken)
          .build();
    }

  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AccessToken {

    private Long id;
    private String email;
    private String nickname;
    private MemberType memberType;
    private String accessToken;
    private String refreshToken;


    public static AccessToken of(Member member, String accessToken) {
      return AccessToken.builder()
          .id(member.getId())
          .email(member.getEmail())
          .nickname(member.getNickname())
          .memberType(member.getMemberType())
          .refreshToken(member.getRefreshToken())
          .accessToken(accessToken)
          .build();
    }

  }


}
