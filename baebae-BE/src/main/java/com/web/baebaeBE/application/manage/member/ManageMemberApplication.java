package com.web.baebaeBE.application.manage.member;

import com.web.baebaeBE.domain.manage.member.service.ManageMemberService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberRequest;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.domain.member.exception.MemberError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ManageMemberApplication {

    private final ManageMemberService manageMemberService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ManageMemberResponse.MemberInformationResponse getMember(Long memberId) {
        return manageMemberService.getMember(memberId);
    }

    public ManageMemberResponse.ProfileImageResponse getProfileImage(Long memberId) {
        String imageUrl = manageMemberService.getProfileImage(memberId);
        return ManageMemberResponse.ProfileImageResponse.of(imageUrl);
    }

    public ManageMemberResponse.ObjectUrlResponse updateProfileImage(Long memberId, MultipartFile image) throws IOException {
        manageMemberService.updateProfileImage(memberId, image);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));
        return ManageMemberResponse.ObjectUrlResponse.of(member.getProfileImage());
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

    public ManageMemberResponse.MemberIdResponse getMemberIdByNickname(String nickname) {
        return manageMemberService.getMemberIdByNickname(nickname);
    }

}
