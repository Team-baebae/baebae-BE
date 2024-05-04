package com.web.baebaeBE.infra.answer.repository;

import com.web.baebaeBE.infra.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findAllByMemberId(Long memberId, Pageable pageable);
}

