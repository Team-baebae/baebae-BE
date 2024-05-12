package com.web.baebaeBE.domain.member.repository;

import com.web.baebaeBE.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


  Optional<Member> findByEmail(String email);
  Optional<Member> findByNickname(String nickname);

  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);

  Optional<Member> findByRefreshToken(String refreshToken);
}

