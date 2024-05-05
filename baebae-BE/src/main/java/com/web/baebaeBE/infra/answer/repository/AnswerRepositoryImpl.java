package com.web.baebaeBE.infra.answer.repository;

import com.web.baebaeBE.infra.answer.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public class AnswerRepositoryImpl implements AnswerRepository{
    private final AnswerJpaRepository jpaRepository;

    @Autowired
    public AnswerRepositoryImpl(AnswerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Answer> findByAnswerId(Long answerId) {
        return jpaRepository.findById(answerId);
    }

    @Override
    public Answer save(Answer answer) {
        return jpaRepository.save(answer);
    }

    @Override
    public Page<Answer> findAllByMemberId(Long memberId, Pageable pageable) {
        return jpaRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public void delete(Answer answerEntity) {
        jpaRepository.delete(answerEntity);
    }
}