package com.web.baebaeBE.infra.category.repository;

import com.web.baebaeBE.infra.category.entity.Category;
import com.web.baebaeBE.infra.category.repository.CategoryRepository;
import com.web.baebaeBE.infra.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByMember(Member member);
}