package com.web.baebaeBE.login.api;


import com.web.baebaeBE.login.application.MemberService;
import com.web.baebaeBE.login.application.OAuth2UserCustomService;
import com.web.baebaeBE.login.domain.Member;
import com.web.baebaeBE.login.dto.LoginRequest;
import com.web.baebaeBE.login.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 클라이언트로부터 소셜 로그인을 받는 컨트롤러
 * localhost:8080/api/oauth/login
 * {
 *     Body : "memberType" : "KAKAO"만 있고 Authorization 안붙이는 경우 -> "Authorization Member가 빈값입니다"
 *     Authrorization과 같이 보내는데 앞에 Bearer 안붙이는 경우 -> "인증 타입이 Bearer 타입이 아닙니다"
 *
 *     grantType, accessToken, accessTokenExpireTime, refreshToken, refreshTokenExpireTime 반환
 *
 * }
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class LoginController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<LoginResponse.SignUp> oauthSignUp(
            @RequestBody LoginRequest.SignUp signUpRequest,
            HttpServletRequest httpServletRequest
    ) {
        // KaKao accessToken 검증 및 추출
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            accessToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출

        System.out.println(accessToken);
        return ResponseEntity.ok(memberService.signUp(accessToken,signUpRequest));
    }








}
