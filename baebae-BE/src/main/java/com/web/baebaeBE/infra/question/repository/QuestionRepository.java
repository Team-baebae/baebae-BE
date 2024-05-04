package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
    Optional<Question> findById(Long questionId);
    Question save(Question questionEntity);
    Page<Question> findAllBySenderId(Long senderId, Pageable pageable);
    Page<Question> findAllByReceiverId(Long receiverId, Pageable pageable);
    void delete(Question questionEntity);

}

