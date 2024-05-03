package com.web.baebaeBE.presentation.manage.member;


import com.web.baebaeBE.application.manage.member.ManageMemberApplication;
import com.web.baebaeBE.presentation.manage.member.api.ManageMemberApi;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberRequest;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class ManageMemberController implements ManageMemberApi {


  private final ManageMemberApplication manageMemberApplication;


  @GetMapping("/{memberId}")
  public ResponseEntity<ManageMemberResponse.MemberInformationResponse> getMember(
          @PathVariable Long memberId
  ) {
    ManageMemberResponse.MemberInformationResponse memberInformation
            = manageMemberApplication.getMember(memberId);

    return ResponseEntity.ok(memberInformation);
  }

  @PatchMapping(value = "/profile-image/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> updateProfileImage(
          @PathVariable Long memberId,
          @RequestPart(value = "image") MultipartFile image
  ) {
    manageMemberApplication.updateProfileImage(memberId, image);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/fcm-token/{memberId}")
  public ResponseEntity<Void> updateFcmToken(
          @PathVariable Long memberId,
          @RequestBody ManageMemberRequest.UpdateFcmTokenDto updateFcmTokenDto
  ) {
    manageMemberApplication.updateFcmToken(memberId, updateFcmTokenDto);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/nickname/{memberId}")
  public ResponseEntity<Void> updateNickname(
          @PathVariable Long memberId,
          @RequestBody ManageMemberRequest.UpdateNicknameDto updateNicknameDto) {
    manageMemberApplication.updateNickname(memberId, updateNicknameDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{memberId}")
  public ResponseEntity<Void> deleteMember(
          @PathVariable Long memberId,
          HttpServletRequest httpServletRequest
  ) {
    manageMemberApplication.deleteMember(memberId, httpServletRequest);
    return ResponseEntity.ok().build();
  }
}


