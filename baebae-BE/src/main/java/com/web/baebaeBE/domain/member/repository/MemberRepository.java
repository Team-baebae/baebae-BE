package com.web.baebaeBE.domain.member.repository;

import com.web.baebaeBE.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


  Optional<Member> findByEmail(String email);
  Optional<Member> findByNickname(String nickname);

  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);

  Optional<Member> findByRefreshToken(String refreshToken);

  // 닉네임이 포함된 회원 목록 조회 (대소문자 구분 없이 검색)
  Page<Member> findByNicknameStartingWithIgnoreCase(String nickname, Pageable pageable);

}

