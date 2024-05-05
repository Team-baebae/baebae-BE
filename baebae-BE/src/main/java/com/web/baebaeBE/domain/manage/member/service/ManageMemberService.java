package com.web.baebaeBE.domain.manage.member.service;

import com.web.baebaeBE.domain.manage.member.exception.ManageMemberError;
import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManageMemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3ImageStorageService s3ImageStorageService;

    public ManageMemberResponse.MemberInformationResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        return ManageMemberResponse.MemberInformationResponse.of(member);
    }

    public void updateProfileImage(Long memberId, String imageUrl) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));
        member.updateProfileImage(imageUrl);
        memberRepository.save(member);
    }

    // 이미지 파일을 Object Storage에 저장하고 키파일을 반환하는 메서드
    // 추후 수정할 예정
    public String convertImageToObject(Long memberId, MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new BusinessException(ManageMemberError.INVAILD_IMAGE_FILE);
        }
        String fileType = "profile";
        int index = 0;
        String fileName = "profile.jpg";
        String path = memberId + "/" + fileName;

        try (InputStream inputStream = image.getInputStream()) {
            long size = image.getSize();
            String contentType = image.getContentType();
            return s3ImageStorageService.uploadFile(memberId.toString(), null, fileType, index, inputStream, size, contentType);
        }
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

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public void verifyMemberWithToken(Long memberId,String accessToken){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        String memberEmail = jwtTokenProvider.getUserEmail(accessToken);

        //회원 정보와 토큰안의 이메일 정보가 일치하지않으면 예외 발생
        if(!member.getEmail().equals(memberEmail))
            throw new BusinessException(ManageMemberError.NOT_VERIFY_MEMBET_WITH_TOKEN);
    }
}
