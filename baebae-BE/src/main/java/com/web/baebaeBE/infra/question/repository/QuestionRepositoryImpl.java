package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository{
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
    public Page<Question> findAllByMemberId(Long memberId, Pageable pageable) {
        return questionJpaRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public Page<Question> findAllByMemberIdAndIsAnsweredTrue(Long memberId, Pageable pageable) {
        // 이 메서드는 답변된 질문만 필터링하여 반환합니다.
        return questionJpaRepository.findAllByMemberIdAndIsAnsweredTrue(memberId, pageable);
    }

    @Override
    public Page<Question> findAllByMemberIdAndIsAnsweredFalse(Long memberId, Pageable pageable) {
        // 이 메서드는 답변되지 않은 질문만 필터링하여 반환합니다.
        return questionJpaRepository.findAllByMemberIdAndIsAnsweredFalse(memberId, pageable);
    }
    @Override
    public void delete(Question questionEntity) {
        questionJpaRepository.delete(questionEntity);
    }
}


