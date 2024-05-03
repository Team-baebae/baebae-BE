package com.web.baebaeBE.application.manage.member;

import com.web.baebaeBE.domain.manage.member.service.ManageMemberService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberRequest;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ManageMemberApplication {

    private final ManageMemberService manageMemberService;
    private final JwtTokenProvider jwtTokenProvider;

    public ManageMemberResponse.MemberInformationResponse getMember(Long memberId) {
        return manageMemberService.getMember(memberId);
    }

    public void updateProfileImage(Long memberId, MultipartFile image) {
        String fileKey = manageMemberService.convertImageToObject(memberId, image);
        manageMemberService.updateProfileImage(memberId, fileKey);
    }

    public void updateFcmToken(Long memberId, ManageMemberRequest.UpdateFcmTokenDto updateFcmTokenDto) {
        manageMemberService.updateFcmToken(memberId, updateFcmTokenDto.getFcmToken());
    }

    public void updateNickname(Long memberId, ManageMemberRequest.UpdateNicknameDto updateNicknameDto) {
        manageMemberService.updateNickname(memberId, updateNicknameDto.getNickname());
    }

    public void deleteMember(Long memberId, HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.getToken(httpServletRequest);
        manageMemberService.verifyMemberWithToken(memberId, accessToken);
        manageMemberService.deleteMember(memberId);
    }

}
