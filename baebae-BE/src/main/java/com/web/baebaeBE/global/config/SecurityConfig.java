package com.web.baebaeBE.global.config;

import com.web.baebaeBE.global.jwt.JwtAuthenticationFilter;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // REST이므로 사용 X
                .csrf().disable() // csrf 보안 해제
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 X
                .and()


                .authorizeHttpRequests()
                // 해당 API에 대해서는 모든 요청을 허가
                .requestMatchers("/api/members/login").permitAll()
                .requestMatchers("/api/members/sign-up").permitAll()
                // USER 권한이 있어야 요청할 수 있음
                .requestMatchers("api/members/test").hasRole("USER")
                // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                .anyRequest().authenticated()
                .and()


                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}