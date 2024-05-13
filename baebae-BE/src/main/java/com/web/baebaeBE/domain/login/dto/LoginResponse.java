package com.web.baebaeBE.domain.login.dto;

import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 시 컨트롤러에서 사용할 response, request dto 작성
 */
public class LoginResponse {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignUpResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "tioon74@gmail.com")
    private String email;
    @Schema(example = "김예찬")
    private String nickname;
    @Schema(example = "KAKAO")
    private MemberType memberType;
    private String profileImage;
    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiZWJlLXNlcnZlciIsImlhdCI6MTcxMzQxNjgyNSwiZXhwIjoxNzEzNTAzMjI1LCJzdWIiOiJ1amozOTAwQG5hdmVyLmNvbSIsImp0aSI6IjIifQ.wvQR4Uoa8KtMgIDwRn7AwKy60olwnzP33_WLI1l3q4I")
    private String accessToken;
    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiZWJlLXNlcnZlciIsImlhdCI6MTcxMzQxNjgyNSwiZXhwIjoxNzE0NjI2NDI1LCJzdWIiOiJ1amozOTAwQG5hdmVyLmNvbSIsImp0aSI6IjIifQ.BYrRkhwK1SSAe3nanmRIT_oSZkWyZlNnl3wFLI_nIqY")
    private String refreshToken;


    public static SignUpResponse of(Member member, String accessToken) {
      return SignUpResponse.builder()
          .id(member.getId())
          .email(member.getEmail())
          .nickname(member.getNickname())
          .memberType(member.getMemberType())
              .profileImage(member.getProfileImage())
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
  public static class AccessTokenResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "tioon74@gmail.com")
    private String email;
    @Schema(example = "김예찬")
    private String nickname;
    @Schema(example = "KAKAO")
    private MemberType memberType;
    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiZWJlLXNlcnZlciIsImlhdCI6MTcxMzQxNjgyNSwiZXhwIjoxNzEzNTAzMjI1LCJzdWIiOiJ1amozOTAwQG5hdmVyLmNvbSIsImp0aSI6IjIifQ.wvQR4Uoa8KtMgIDwRn7AwKy60olwnzP33_WLI1l3q4I")
    private String accessToken;
    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiZWJlLXNlcnZlciIsImlhdCI6MTcxMzQxNjgyNSwiZXhwIjoxNzE0NjI2NDI1LCJzdWIiOiJ1amozOTAwQG5hdmVyLmNvbSIsImp0aSI6IjIifQ.BYrRkhwK1SSAe3nanmRIT_oSZkWyZlNnl3wFLI_nIqY")
    private String refreshToken;
    public static AccessTokenResponse of(Member member, String accessToken) {
      return AccessTokenResponse.builder()
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
  public static class isExistingUserResponse {
    private Boolean isExisting;
  }
}
