package com.web.baebaeBE.global.jwt;


import com.web.baebaeBE.global.error.BusinessException;
import com.web.baebaeBE.global.jwt.exception.TokenError;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor // 토큰 필터 클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization"; //HTTP 요청 헤더에서 Authorization 정보를 가지고 옴.
    private final static String TOKEN_PREFIX = "Bearer "; // 토큰 접두사 지정.


    //필터 메서드 (HTTP 요청이 올 때마다 실행됨) (스프링에서 실행되는게 아닌, Web Context에서 필터링하는 것.)
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // 요청
            HttpServletResponse response, // 응답
            FilterChain filterChain)  throws ServletException, IOException { // 필터체인

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader); // 헤더에서 토큰 정보를 가지고 옴

        // 토큰 값 존재 확인
        if (token != null && tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        //필터 체인 계속 실행
        filterChain.doFilter(request, response);
    }

    //Authorization 헤더에서 토큰 정보를 가져오는 메서드.
    private String getAccessToken(String authorizationHeader) {

        if(authorizationHeader == null) // AuthorizationHeader가 NULL일 경우
            throw new BusinessException(TokenError.NOT_EXISTS_AUTHORIZATION);

        else if(!authorizationHeader.startsWith(TOKEN_PREFIX)) // 토큰 인증타입이 Bearer가 아닌경우
            throw new BusinessException(TokenError.NOT_VALID_BEARER_GRANT_TYPE);

        else
            return authorizationHeader.substring(TOKEN_PREFIX.length()); // 토큰 추출
    }
}

