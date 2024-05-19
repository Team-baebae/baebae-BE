package com.web.baebaeBE.domain.other;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Other", description = "기타")
public class OtherController {

    @GetMapping("/healthcheck")
    @Operation(summary = "Health Check", description = "Load Balncer용 API입니다. (사용X)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서버 상태 정상")
            })
    public String healthCheck() {
        return "OK"; // 서버가 정상적으로 작동 중임을 의미
    }




    @GetMapping("api/test")
    @Operation(summary = "백엔드용 TEST API입니다. (사용X)")
    @ResponseBody
    public void test(HttpServletRequest request) {
        //System.out.println(request.getAttribute("id"));
        return;
    }



}