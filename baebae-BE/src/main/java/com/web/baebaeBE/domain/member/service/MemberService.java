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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        String imageUrl = convertImageToObject(memberId, image);
        member.updateProfileImage(imageUrl);
        memberRepository.save(member);

        return MemberResponse.ProfileImageResponse.of(imageUrl);
    }

    public String convertImageToObject(Long memberId, MultipartFile image){
        if (image.isEmpty()) {
            throw new BusinessException(MemberException.INVAILD_IMAGE_FILE);
        }
        String fileType = "profile";
        int index = 0; // 프로필 이미지에는 인덱스가 필요 없으므로 사용하지 않음
        String fileName = memberId + "_profile.jpg";

        try (InputStream inputStream = image.getInputStream()) {
            long size = image.getSize();
            String contentType = image.getContentType();
            return s3ImageStorageService.uploadFile(memberId.toString(), null, fileType, index, inputStream, size, contentType);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    public void updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        member.update(nickname);
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }


    // 닉네임 기반으로 memberId를 찾아오는 메서드
    public MemberResponse.MemberIdResponse getMemberIdByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(MemberException.NOT_EXIST_MEMBER));

        return  MemberResponse.MemberIdResponse.of(member.getId());
    }
}
