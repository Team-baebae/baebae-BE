package com.web.baebaeBE.domain.Follow.service;

import com.web.baebaeBE.domain.Follow.dto.FollowResponse;
import com.web.baebaeBE.domain.Follow.entity.Follow;
import com.web.baebaeBE.domain.Follow.entity.relationType;
import com.web.baebaeBE.domain.Follow.entity.statusType;
import com.web.baebaeBE.domain.Follow.exception.FollowException;
import com.web.baebaeBE.domain.Follow.repository.FollowRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.exception.MemberException;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void followMember(Long followerId, Long followingId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new BusinessException(MemberException.NOT_EXIST_MEMBER));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new BusinessException(MemberException.NOT_EXIST_MEMBER));

        //이미 팔로우 관계가 있는지 확인
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new BusinessException(FollowException.ALREADY_EXISTS_FOLLOW);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .status(statusType.APPROVED) // 현재는 승인만 존재 (2025.02.28)
                .relation(relationType.NEW)
                .createdAt(LocalDateTime.now())
                .build();

        followRepository.save(follow);
    }

    public void deleteFollower(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new BusinessException(FollowException.NOT_EXIST_FOLLOW));

        followRepository.delete(follow);
    }

    // 나를 팔로우하는 사람들 리스트
    public Page<FollowResponse.FollowMemberResponse> getFollowerList(Long memberId, Pageable page) {
        return followRepository.findAllFollowersByMemberId(memberId, page)
                .map(member -> FollowResponse.FollowMemberResponse.of(member));

    }

    // 내가 팔로우하는 사람들 리스트
    public Page<FollowResponse.FollowMemberResponse> getFollowingList(Long memberId, Pageable page) {
        return followRepository.findAllFollowingsByMemberId(memberId, page)
                .map(member -> FollowResponse.FollowMemberResponse.of(member));

    }

    public FollowResponse.isFollowingResponse isFollowing(Long followerId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        return FollowResponse.isFollowingResponse.of(follow.isPresent());
    }
}
