package com.web.baebaeBE.domain.member.service;

import com.web.baebaeBE.domain.member.exception.MemberException;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.member.dto.MemberResponse;
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
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3ImageStorageService s3ImageStorageService;

    public MemberResponse.MemberInformationResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        return MemberResponse.MemberInformationResponse.of(member);
    }
    public MemberResponse.ProfileImageResponse getProfileImage(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));
        return MemberResponse.ProfileImageResponse.of(member.getProfileImage());
    }

    public MemberResponse.ProfileImageResponse updateProfileImage(Long memberId, MultipartFile image) {
        String imageUrl = null;
        try {
            imageUrl = convertImageToObject(memberId, image);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));
        member.updateProfileImage(imageUrl);
        memberRepository.save(member);

        return MemberResponse.ProfileImageResponse.of(imageUrl);
    }

    public String convertImageToObject(Long memberId, MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new BusinessException(MemberException.INVAILD_IMAGE_FILE);
        }
        String fileType = "profile";
        int index = 0; // 프로필 이미지에는 인덱스가 필요 없으므로 사용하지 않음
        String fileName = memberId + "_profile.jpg";
        String path = memberId + "/" + fileName;

        try (InputStream inputStream = image.getInputStream()) {
            long size = image.getSize();
            String contentType = image.getContentType();
            return s3ImageStorageService.uploadFile(memberId.toString(), null, fileType, index, inputStream, size, contentType);
        }
    }

    public void updateFcmToken(Long memberId, String fcmToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        member.updateFcmToken(fcmToken);
        memberRepository.save(member);
    }

    public void updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        member.update(nickname);
        memberRepository.save(member);
    }

    public void deleteMember(Long id, HttpServletRequest httpServletRequest) {
        String accessToken = jwtTokenProvider.getToken(httpServletRequest);
        verifyMemberWithToken(id, accessToken);

        memberRepository.deleteById(id);
    }

    public void verifyMemberWithToken(Long memberId,String accessToken){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        String memberEmail = jwtTokenProvider.getUserEmail(accessToken);

        //회원 정보와 토큰안의 이메일 정보가 일치하지않으면 예외 발생
        if(!member.getEmail().equals(memberEmail))
            throw new BusinessException(MemberException.NOT_VERIFY_MEMBET_WITH_TOKEN);
    }

    // 닉네임 기반으로 memberId를 찾아오는 메서드
    public MemberResponse.MemberIdResponse getMemberIdByNickname(String nickname) {
        return MemberResponse.MemberIdResponse.of(memberRepository.findByNickname(nickname).get().getId());
    }
}
