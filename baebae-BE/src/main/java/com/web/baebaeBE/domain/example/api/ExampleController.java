package com.web.baebaeBE.domain.example.api;

import com.web.baebaeBE.domain.example.application.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/example")
@Validated
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping("/new")
    public ResponseEntity<String> ExampleController(){

        return ResponseEntity.ok("Success");
    }
}
