package com.web.baebaeBE.domain.category.repository;

import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByMember(Member member);
}