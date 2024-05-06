package com.web.baebaeBE.infra.categorized.answer.repository;

import com.web.baebaeBE.infra.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.infra.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorizedAnswerRepository extends JpaRepository<CategorizedAnswer, Long> {
    List<CategorizedAnswer> findAllByCategory(Category category);
}