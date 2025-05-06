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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public FollowResponse.FollowCountResponse getFollowerCount(Long memberId) {
        long followerCount = followRepository.countByFollowing_Id(memberId);
        long followingCount = followRepository.countByFollower_Id(memberId);

        return FollowResponse.FollowCountResponse.of(followerCount, followingCount);
    }

    public void deleteFollower(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new BusinessException(FollowException.NOT_EXIST_FOLLOW));

        followRepository.delete(follow);
    }

    // 나를 팔로우하는 사람들 리스트
    public Page<FollowResponse.FollowMemberResponse> getFollowerList(Long memberId, Pageable page) {

        Page<Member> members = followRepository.findAllFollowersByMemberId(memberId, page);

        // 내가 팔로우하는 사람들의 ID 목록을 Set으로 조회
        Set<Long> followingSet = new HashSet<>(followRepository.findAllFollowingIdsByMemberId(memberId));

        // 팔로워 목록을 응답 객체로 변환 (각 팔로워에 대해 내가 팔로우하는지 확인)
        return members.map(member ->
                FollowResponse.FollowMemberResponse.of(member, followingSet.contains(member.getId()))
        );

    }

    // 내가 팔로우하는 사람들 리스트
    public Page<FollowResponse.FollowMemberResponse> getFollowingList(Long memberId, Pageable page) {
        return followRepository.findAllFollowingsByMemberId(memberId, page)
                .map(member -> FollowResponse.FollowMemberResponse.of(member, true));

    }

    public FollowResponse.isFollowingResponse isFollowing(Long followerId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        return FollowResponse.isFollowingResponse.of(follow.isPresent());
    }

    public FollowResponse.hasNewFollowersResponse hasFollowers(Long memberId){
        boolean hasNewFollower = followRepository.existsByFollowingIdAndRelation(memberId, relationType.NEW);
        return FollowResponse.hasNewFollowersResponse.of(hasNewFollower);
    }

    public void updateAllRelationsToExisting(Long memberId) {
        followRepository.updateNewRelationToExistingByMemberId(memberId, relationType.EXISTING, relationType.NEW);
    }
}
