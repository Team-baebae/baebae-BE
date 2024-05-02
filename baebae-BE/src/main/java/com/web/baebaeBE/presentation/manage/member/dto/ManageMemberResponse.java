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




}
