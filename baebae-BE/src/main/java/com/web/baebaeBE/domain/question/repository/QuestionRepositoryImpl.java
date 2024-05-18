package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public Optional<Question> findById(Long questionId) {
        return questionJpaRepository.findById(questionId);
    }

    @Override
    public Question save(Question questionEntity) {
        return questionJpaRepository.save(questionEntity);
    }

    @Override
    public Page<Question> findAllByReceiverId(Long receiverId, Pageable pageable) {
        return questionJpaRepository.findAllByReceiverId(receiverId, pageable);
    }

    @Override
    public Page<Question> findAllByReceiverIdAndIsAnsweredTrue(Long receiverId, Pageable pageable) {
        return questionJpaRepository.findAllByReceiverIdAndIsAnsweredTrue(receiverId, pageable);
    }

    @Override
    public Page<Question> findAllByReceiverIdAndIsAnsweredFalse(Long receiverId, Pageable pageable) {
        return questionJpaRepository.findAllByReceiverIdAndIsAnsweredFalse(receiverId, pageable);
    }

    @Override
    public void delete(Question questionEntity) {
        questionJpaRepository.delete(questionEntity);
    }

    @Override
    public long countByReceiverIdAndIsAnsweredFalse( Long receiverId) {
        return questionJpaRepository.countByReceiverIdAndIsAnsweredFalse(receiverId);
    }
}