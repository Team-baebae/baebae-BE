package com.web.baebaeBE.kakao.api;

import com.web.baebaeBE.kakao.dto.KakaoDto;
import com.web.baebaeBE.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping()
public class KakaoController {

    private final KakaoService kakaoService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    @GetMapping("/api/oauth/kakao")
    public String login(){
        return "oauthLogin";
    }



    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<KakaoDto.Response> loginCallback(@RequestParam("code")String code) {
        KakaoDto.Response kakaoToken = kakaoService.loginCallback(code);
        return ResponseEntity.ok(kakaoToken);
    }



    @GetMapping("/test")
    @ResponseBody
    public void test(HttpServletRequest request) {
        //System.out.println(request.getAttribute("id"));
        return;
    }

}
