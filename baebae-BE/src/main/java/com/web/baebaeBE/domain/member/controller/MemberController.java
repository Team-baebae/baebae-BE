package com.web.baebaeBE.domain.member.controller;


import com.web.baebaeBE.domain.member.service.MemberService;
import com.web.baebaeBE.domain.member.controller.api.MemberApi;
import com.web.baebaeBE.domain.member.dto.MemberRequest;
import com.web.baebaeBE.domain.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController implements MemberApi {


  private final MemberService memberService;


  @GetMapping("/{memberId}")
  public ResponseEntity<MemberResponse.MemberInformationResponse> getMemberInformation(
          @PathVariable Long memberId
  ) {

    return ResponseEntity.ok(memberService.getMember(memberId));
  }

  @GetMapping("/nickname/{nickname}")
  public ResponseEntity<MemberResponse.MemberIdResponse> getMemberIdByNickname(
          @PathVariable String nickname
  ) {

    return ResponseEntity.ok(memberService.getMemberIdByNickname(nickname));
  }

  @GetMapping("/profile-image/{memberId}")
  public ResponseEntity<MemberResponse.ProfileImageResponse> getProfileImage(
          @PathVariable Long memberId
  ) {

    return ResponseEntity.ok(memberService.getProfileImage(memberId));
  }


  @PatchMapping(value = "/profile-image/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MemberResponse.ProfileImageResponse> updateProfileImage(
          @PathVariable Long memberId,
          @RequestPart(value = "image") MultipartFile image) {

    return ResponseEntity.ok(memberService.updateProfileImage(memberId, image));
  }


  @PatchMapping("/nickname/{memberId}")
  public ResponseEntity<Void> updateNickname(
          @PathVariable Long memberId,
          @RequestBody MemberRequest.UpdateNicknameDto updateNicknameDto) {

    memberService.updateNickname(memberId, updateNicknameDto.getNickname());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{memberId}")
  public ResponseEntity<Void> deleteMember(
          @PathVariable Long memberId,
          HttpServletRequest httpServletRequest) {

    memberService.deleteMember(memberId, httpServletRequest);
    return ResponseEntity.ok().build();
  }


}


