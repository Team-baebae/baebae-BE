package com.web.baebaeBE.domain.reactioncount.repository;

import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionCountJpaRepository extends JpaRepository<ReactionCount, Long> {
    ReactionCount findByAnswerId(Long answerId);
}
