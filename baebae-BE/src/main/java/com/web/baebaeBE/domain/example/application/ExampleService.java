package com.web.baebaeBE.domain.example.application;

import com.web.baebaeBE.domain.example.dao.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRepository exampleRepository;
}
