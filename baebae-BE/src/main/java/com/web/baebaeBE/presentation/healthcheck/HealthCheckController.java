package com.web.baebaeBE.presentation.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "OK"; // 서버가 정상적으로 작동 중임을 의미
    }
}