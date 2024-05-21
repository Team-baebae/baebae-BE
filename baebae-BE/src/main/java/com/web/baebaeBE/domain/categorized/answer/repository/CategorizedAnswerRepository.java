package com.web.baebaeBE.domain.categorized.answer.repository;

import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategorizedAnswerRepository extends JpaRepository<CategorizedAnswer, Long> {
    Page<CategorizedAnswer> findByAnswer_Member_IdAndCategory_Id(Long memberId, Long categoryId, Pageable pageable);
    Page<CategorizedAnswer> findByAnswer_Member_Id(Long memberId, Pageable pageable);
    List<CategorizedAnswer> findAllByAnswerId(Long answerId);
}
