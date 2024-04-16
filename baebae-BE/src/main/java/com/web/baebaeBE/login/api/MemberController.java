package com.web.baebaeBE.login.api;


import com.web.baebaeBE.login.application.MemberService;
import com.web.baebaeBE.login.dto.MemberRequest;
import com.web.baebaeBE.login.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
@RequestMapping("/api/oauth")
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse.SignUp> oauthSignUp(
            @RequestBody MemberRequest.SignUp signUpRequest,
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


    // Access Token 재발급
    @GetMapping("/access-token/issue")
    public ResponseEntity<MemberResponse.AccessToken> refreshToken(HttpServletRequest httpServletRequest
    ) {
        // Refresh Token 검증 및 추출
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String refreshToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            refreshToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출


        // Access Token 재발급
        return ResponseEntity.ok(memberService.newAccessToken(refreshToken));
    }







}
