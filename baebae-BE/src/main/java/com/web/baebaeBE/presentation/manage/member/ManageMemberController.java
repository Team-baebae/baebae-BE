package com.web.baebaeBE.presentation.manage.member;


import com.web.baebaeBE.application.manage.member.ManageMemberApplication;
import com.web.baebaeBE.presentation.manage.member.api.ManageMemberApi;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberRequest;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class ManageMemberController implements ManageMemberApi {


  private final ManageMemberApplication manageMemberApplication;


  @GetMapping("/{id}")
  public ResponseEntity<ManageMemberResponse.MemberInformationResponse> getMember(
          @PathVariable Long id
  ) {
    ManageMemberResponse.MemberInformationResponse memberInformation
            = manageMemberApplication.getMember(id);

    return ResponseEntity.ok(memberInformation);
  }

  @PatchMapping("/profile-image/{id}")
  public ResponseEntity<Void> updateProfileImage(
          @PathVariable Long id,
          @RequestBody ManageMemberRequest.UpdateProfileImageDto updateProfileImageDto
  ) {
    manageMemberApplication.updateProfileImage(id, updateProfileImageDto);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/fcm-token/{id}")
  public ResponseEntity<Void> updateFcmToken(
          @PathVariable Long id,
          @RequestBody ManageMemberRequest.UpdateFcmTokenDto updateFcmTokenDto
  ) {
    manageMemberApplication.updateFcmToken(id, updateFcmTokenDto);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/nickname/{id}")
  public ResponseEntity<Void> updateNickname(
          @PathVariable Long id,
          @RequestBody ManageMemberRequest.UpdateNicknameDto updateNicknameDto) {
    manageMemberApplication.updateNickname(id, updateNicknameDto);
    return ResponseEntity.ok().build();
  }




}
