package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllBySenderId(Long senderId, Pageable pageable);
    Page<Question> findAllByReceiverId(Long receiverId, Pageable pageable);
}
