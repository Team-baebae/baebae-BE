package com.web.baebaeBE.domain.Follow.repository;


import com.web.baebaeBE.domain.Follow.entity.Follow;
import com.web.baebaeBE.domain.Follow.entity.relationType;
import com.web.baebaeBE.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    // 중복 체크에 사용할 메서드
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

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

    boolean existsByFollowingIdAndRelation(Long memberId, relationType relation);

    @Modifying
    @Query("UPDATE Follow f SET f.relation = :existingRelation WHERE f.following.id = :memberId AND f.relation = :newRelation")
    void updateNewRelationToExistingByMemberId(@Param("memberId") Long memberId,
                                          @Param("existingRelation") relationType existingRelation,
                                          @Param("newRelation") relationType newRelation);
}
