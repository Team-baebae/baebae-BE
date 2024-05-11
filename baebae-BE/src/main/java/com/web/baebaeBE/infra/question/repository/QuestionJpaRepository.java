package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);

    // 답변 된 질문 조회
//    @Query("SELECT q FROM Question q JOIN q.answers a WHERE q.member.id = :memberId")
    Page<Question> findAllAnsweredQuestionsByMemberId(Long memberId, Pageable pageable);

    // 답변 안된 질문 조회
//    @Query("SELECT q FROM Question q LEFT JOIN q.answers a WHERE q.member.id = :memberId AND a.id IS NULL")
    Page<Question> findAllUnansweredQuestionsByMemberId(Long memberId, Pageable pageable);
}
