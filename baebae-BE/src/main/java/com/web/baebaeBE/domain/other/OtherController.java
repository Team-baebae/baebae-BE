package com.web.baebaeBE.domain.other;

import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Other", description = "기타")
public class OtherController {

    private final S3ImageStorageService s3ImageStorageService;

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

    @GetMapping("/api/image")
    @Operation(
            summary = "이미지 추출 API",
            description = "S3에 저장된 이미지를 추출합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    public ResponseEntity<Map<String, String>> getFile(@RequestParam String url) throws IOException{
        InputStream inputStream = s3ImageStorageService.getFileData(url);
        byte[] bytes = inputStream.readAllBytes();
        String encodedImage = Base64.getEncoder().encodeToString(bytes);

        Map<String, String> response = new HashMap<>();
        response.put("image", encodedImage);

        return ResponseEntity.ok(response);
    }

}