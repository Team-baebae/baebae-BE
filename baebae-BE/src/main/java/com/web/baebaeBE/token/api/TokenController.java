package com.web.baebaeBE.token.api;

import com.web.baebaeBE.token.dto.TokenDto;
import com.web.baebaeBE.token.service.TokenService;
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
public class TokenController {

    private final TokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    @GetMapping("/api/oauth/kakao")
    public String login(){
        return "oauthLogin";
    }



    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<TokenDto.Response> loginCallback(@RequestParam("code")String code) {
        TokenDto.Response kakaoToken = tokenService.loginCallback(code);
        return ResponseEntity.ok(kakaoToken);
    }



    @GetMapping("/test")
    @ResponseBody
    public void test(HttpServletRequest request) {
        //System.out.println(request.getAttribute("id"));
        return;
    }

}
