package com.web.baebaeBE.global.config;

import com.web.baebaeBE.global.jwt.JwtAuthenticationFilter;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.service.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserCustomService oAuth2UserCustomService;



    // Spring Security 제외 목록 (인증,인가 검사 제외)
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> {
            web.ignoring()
                    .requestMatchers(toH2Console())
                    .requestMatchers(
                    "/api/oauth/kakao",
                            "/favicon.ico",
                            "/oauth/kakao/callback",
                            "/api/oauth/sign-up",
                            "swagger-ui.html",
                            "/swagger-ui/**"
                    )
                    //.requestMatchers("/img/**", "/css/**", "/js/**") // 해당 URL 보안검사 무시.
                    .requestMatchers("/**");
        };
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
                //**//
            // 이 밖에 모든 요청에 대해서 인증 필요
            .anyRequest().authenticated();

        //JWT 검증 필터 등록
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //H2 Security 제외 설정
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }




}