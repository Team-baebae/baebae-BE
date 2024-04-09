package com.web.baebaeBE.login.application;

import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.login.dao.MemberRepository;
import com.web.baebaeBE.login.domain.Member;
import com.web.baebaeBE.login.dto.Request;
import com.web.baebaeBE.login.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Response.LoginResponse login(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        Response.LoginResponse loginResponse = jwtTokenProvider.generateToken(authentication);

        return loginResponse;
    }

    @Transactional
    public Response.SignUpResponse signUp(Request.SignUpRequest signUpRequest) {
        if (memberRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  // USER 권한 부여

        //member 저장
        Member member = memberRepository.save(signUpRequest.toEntity(encodedPassword, roles));;

        return Response.SignUpResponse.of(member);
    }






}