package com.web.baebaeBE.global.security;

public final class SecurityConstants {

    public static final String[] NO_AUTH_LIST = {
            // oAuth2 인증 제외
            "/api/oauth/kakao", "/favicon.ico", "/oauth/kakao/callback",
            "/api/auth/login", "/api/auth/isExisting", "/api/auth/nickname/isExisting",
            "/api/member/profile-image/{memberId}", "/api/member/nickname/{nickname}",
            "/api/category/{memberId}", "/api/answers", "/api/answers/member/{memberId}",
            "/api/answers/{answerId}/reactionsCount",
            // Swagger 제외 과정
            "/v3/**", "/swagger-ui/**",
            // Error 페이지
            "/error",
            // 로드밸런스 헬스체크
            "/healthcheck"
    };

    public static final String[] AUTHENTICATED_LIST = {
            // KakaoController
            "api/test",
            // MemberController
            "/api/oauth/login", "/api/oauth/token/refresh", "/api/oauth/logout"
    };
}
