package com.web.baebaeBE.infra.member.repository;

import com.web.baebaeBE.infra.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  boolean existsByEmail(String email);

  Optional<Member> findByRefreshToken(String refreshToken);
}
