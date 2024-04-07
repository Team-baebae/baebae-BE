package com.web.baebaeBE.domain.example.dao;

import com.web.baebaeBE.domain.example.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

}
