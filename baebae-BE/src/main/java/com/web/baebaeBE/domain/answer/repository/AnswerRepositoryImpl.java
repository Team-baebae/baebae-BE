package com.web.baebaeBE.domain.answer.repository;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.answer.repository.AnswerJpaRepository;
import com.web.baebaeBE.domain.answer.repository.AnswerRepository;
import com.web.baebaeBE.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class AnswerRepositoryImpl implements AnswerRepository {
    private final AnswerJpaRepository jpaRepository;

    @Autowired
    public AnswerRepositoryImpl(AnswerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public List<Answer> findByMemberId(Long memberId) {
        return jpaRepository.findByMemberId(memberId);
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
    public Page<Answer> findAllByMemberIdAndCategory(Long memberId, Category category, Pageable pageable) {
        return jpaRepository.findAllByMemberIdAndCategory(memberId, category, pageable);
    }
    @Override
    public void delete(Answer answer) {
        jpaRepository.delete(answer);
    }
}

