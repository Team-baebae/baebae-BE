package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
    Page<QuestionEntity> findAllByMemberId(Long memberId, Pageable pageable);
}

