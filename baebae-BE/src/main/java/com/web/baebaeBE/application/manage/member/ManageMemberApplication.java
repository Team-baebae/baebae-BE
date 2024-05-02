package com.web.baebaeBE.application.manage.member;

import com.web.baebaeBE.domain.manage.member.service.ManageMemberService;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberRequest;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ManageMemberApplication {

    private final ManageMemberService memberService;

    public ManageMemberResponse.MemberInformationResponse getMember(Long id) {
        return memberService.getMember(id);
    }

    public void updateProfileImage(Long id, ManageMemberRequest.UpdateProfileImageDto updateProfileImageDto) {
        memberService.updateProfileImage(id, updateProfileImageDto.getProfileImage());
    }

    public void updateFcmToken(Long id, ManageMemberRequest.UpdateFcmTokenDto updateFcmTokenDto) {
        memberService.updateFcmToken(id, updateFcmTokenDto.getFcmToken());
    }

    public void updateNickname(Long id, ManageMemberRequest.UpdateNicknameDto updateNicknameDto) {
        memberService.updateNickname(id, updateNicknameDto.getNickname());
    }

}
