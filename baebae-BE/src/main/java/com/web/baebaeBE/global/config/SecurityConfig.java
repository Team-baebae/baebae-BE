package com.web.baebaeBE.global.config;

import com.web.baebaeBE.global.jwt.JwtAuthenticationFilter;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.login.service.OAuth2UserCustomService;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.web.baebaeBE.global.security.SecurityConstants.NO_AUTH_LIST;
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
                    .requestMatchers(HttpMethod.GET, "/api/member/profile-image/{memberId}")
                    .requestMatchers(HttpMethod.GET, "/api/category/{memberId}")
                    //.requestMatchers(toH2Console())
                    .requestMatchers(NO_AUTH_LIST);
                    //.requestMatchers("/**");
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource()) // cors 활성화
            .and()
            .csrf().disable() // csrf, http로그인, form로그인, 로그아웃 비활성화. (토큰방식으로 인증)
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

    // 스프링 시큐리티 CORS 허용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://api.flipit.co.kr", "https://www.flipit.co.kr", "https://flipit.co.kr")); // 허용할 오리진 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH")); // 허용할 HTTP 메소드 설정
        configuration.setAllowedHeaders(Collections.singletonList("*")); // 허용할 HTTP 헤더 설정
        configuration.setAllowCredentials(true); // 쿠키를 포함한 요청 허용 설정
        configuration.setMaxAge(3600L); // pre-flight 요청의 결과를 캐시하는 시간 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //H2 Security 제외 설정
    /*@Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }*/
}