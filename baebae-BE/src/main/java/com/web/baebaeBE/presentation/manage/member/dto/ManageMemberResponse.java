package com.web.baebaeBE.presentation.manage.member.dto;

import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 로그인 시 컨트롤러에서 사용할 response, request dto 작성
 */
public class ManageMemberResponse {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MemberInformationResponse {
    private Long memberId;
    private String email;
    private String nickname;
    private String profileImage;
    private MemberType memberType;

    public static MemberInformationResponse of (Member member){
      return MemberInformationResponse.builder()
              .memberId(member.getId())
              .email(member.getEmail())
              .nickname(member.getNickname())
              .profileImage(member.getProfileImage())
              .memberType(member.getMemberType())
              .build();
    }
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ObjectUrlResponse{
    private String imageUrl;

    public static ObjectUrlResponse of (String imageUrl){
      return ObjectUrlResponse.builder()
              .imageUrl(imageUrl)
              .build();
    }
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProfileImageResponse {
    private String imageUrl;

    public static ProfileImageResponse of(String imageUrl) {
      ProfileImageResponse response = new ProfileImageResponse();
      response.imageUrl = imageUrl;
      return response;
    }
  }


  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MemberIdResponse{
    private Long memberId;

    public static MemberIdResponse of (Long memberId){
      return MemberIdResponse.builder()
              .memberId(memberId)
              .build();
    }
  }




}
