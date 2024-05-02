package com.web.baebaeBE.domain.manage.member.service;

import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManageMemberService {

    private final MemberRepository memberRepository;

    public ManageMemberResponse.MemberInformationResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        return ManageMemberResponse.MemberInformationResponse.of(member);
    }

    public void updateProfileImage(Long memberId, String profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        member.updateProfileImage(profileImage);
        memberRepository.save(member);
    }

    public void updateFcmToken(Long memberId, String fcmToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        member.updateFcmToken(fcmToken);
        memberRepository.save(member);
    }

    public void updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        member.update(nickname);
        memberRepository.save(member);
    }
}
