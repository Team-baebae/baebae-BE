package com.web.baebaeBE.domain.reactioncount.repository;

import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionCountJpaRepository extends JpaRepository<ReactionCount, Long> {
    Optional<ReactionCount> findByAnswerId(Long answerId);
}
