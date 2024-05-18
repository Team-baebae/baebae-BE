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
    public Page<Question> findAllBySenderIdOrReceiverId(Long senderId, Long receiverId, Pageable pageable) {
        return questionJpaRepository.findAllBySenderIdOrReceiverId(senderId, receiverId, pageable);
    }

    @Override
    public Page<Question> findAllBySenderIdOrReceiverIdAndIsAnsweredTrue(Long senderId, Long receiverId, Pageable pageable) {
        return questionJpaRepository.findAllBySenderIdOrReceiverIdAndIsAnsweredTrue(senderId, receiverId, pageable);
    }

    @Override
    public Page<Question> findAllBySenderIdOrReceiverIdAndIsAnsweredFalse(Long senderId, Long receiverId, Pageable pageable) {
        return questionJpaRepository.findAllBySenderIdOrReceiverIdAndIsAnsweredFalse(senderId, receiverId, pageable);
    }

    @Override
    public void delete(Question questionEntity) {
        questionJpaRepository.delete(questionEntity);
    }

    @Override
    public long countBySenderIdOrReceiverIdAndIsAnsweredFalse(Long senderId, Long receiverId) {
        return questionJpaRepository.countBySenderIdOrReceiverIdAndIsAnsweredFalse(senderId, receiverId);
    }
}