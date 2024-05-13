package com.web.baebaeBE.domain.healthcheck;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "헬스체크용 API")
public class HealthCheckController {

    @GetMapping("/healthcheck")
    @Operation(summary = "Health Check", description = "Load Balncer용 API입니다. (사용X)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서버 상태 정상")
            })
    public String healthCheck() {
        return "OK"; // 서버가 정상적으로 작동 중임을 의미
    }
}