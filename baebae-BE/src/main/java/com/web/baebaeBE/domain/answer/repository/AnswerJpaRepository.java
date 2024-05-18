package com.web.baebaeBE.domain.answer.repository;

import com.web.baebaeBE.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findAllByMemberId(Long memberId, Pageable pageable);
    List<Answer> findByMemberId(Long memberId);
}
