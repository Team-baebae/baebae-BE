package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Question> findAllByMemberIdAndIsAnsweredTrue(Long memberId, Pageable pageable);
    Page<Question> findAllByMemberIdAndIsAnsweredFalse(Long memberId, Pageable pageable);

}
