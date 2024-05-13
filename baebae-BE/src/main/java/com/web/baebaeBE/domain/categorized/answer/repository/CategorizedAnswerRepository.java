package com.web.baebaeBE.domain.categorized.answer.repository;

import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorizedAnswerRepository extends JpaRepository<CategorizedAnswer, Long> {
    Page<CategorizedAnswer> findAllByCategory(Long memberId, Category category, Pageable pageable);
}