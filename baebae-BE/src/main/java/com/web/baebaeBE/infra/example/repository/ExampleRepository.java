package com.web.baebaeBE.infra.example.repository;

import com.web.baebaeBE.infra.example.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

}
