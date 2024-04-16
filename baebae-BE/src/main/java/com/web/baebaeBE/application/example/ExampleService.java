package com.web.baebaeBE.application.example;

import com.web.baebaeBE.infra.example.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRepository exampleRepository;
}
