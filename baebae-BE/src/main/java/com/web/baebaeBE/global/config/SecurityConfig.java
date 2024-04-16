package com.web.baebaeBE.global.config;

import com.web.baebaeBE.global.jwt.JwtAuthenticationFilter;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.login.application.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserCustomService oAuth2UserCustomService;



    // Spring Security 제외 목록 (인증,인가 검사 제외)
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/api/kakao", "/oauth/kakao/callback", "/api/oauth2/sign-up")
                .requestMatchers("/img/**", "/css/**", "/js/**"); // 해당 URL 보안검사 무시.
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // csrf, http로그인, form로그인, 로그아웃 비활성화. (토큰방식으로 인증)
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable();

        http.sessionManagement() // 세션관리 설정 (Stateless로 설정 -> 세션 사용X)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.authorizeHttpRequests()
            // 해당 API에 대해서는 모든 요청을 허가
            .requestMatchers("/api/oauth/kakao").permitAll()
            .requestMatchers("/oauth/kakao/callback").permitAll()
            .requestMatchers("/api/oauth/sign-up").permitAll()
                //.requestMatchers("/api/oauth/access-token/issue").permitAll()
            // 이 밖에 모든 요청에 대해서 인증 필요
            .anyRequest().authenticated();

        //헤더 확인 커스텀 필터 추가.  (JWT 토큰 필터)(tokenAuthenticationFilter)
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*@Bean// OAuth2 로그인 성공시 실행 핸들러 -> OAuth2 성공 후 바로 실행.
    public OAuth2SuccessHandler oAuth2SuccessHandler() {

        // OAuth2SuccessHandler를 실행함.
        return new OAuth2SuccessHandler(jwtTokenProvider,
                memberService,
                memberRepository
        );
    }*/

    @Bean // 토큰 인증 필터
    public JwtAuthenticationFilter tokenAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}