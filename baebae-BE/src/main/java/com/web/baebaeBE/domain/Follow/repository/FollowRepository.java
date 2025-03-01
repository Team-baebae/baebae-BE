package com.web.baebaeBE.domain.Follow.repository;


import com.web.baebaeBE.domain.Follow.entity.Follow;
import com.web.baebaeBE.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT fol " +
            "FROM Follow f " +
            "JOIN f.follower fol " +
            "WHERE f.following.id = :memberId")
    Page<Member> findAllFollowersByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT fol " +
            "FROM Follow f " +
            "JOIN f.following fol " +
            "WHERE f.follower.id = :memberId")
    Page<Member> findAllFollowingsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
