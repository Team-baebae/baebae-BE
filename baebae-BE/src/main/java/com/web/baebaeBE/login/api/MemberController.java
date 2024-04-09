package com.web.baebaeBE.login.api;

import com.web.baebaeBE.global.util.SecurityUtil;
import com.web.baebaeBE.login.dto.Request;
import com.web.baebaeBE.login.application.MemberServiceImpl;
import com.web.baebaeBE.login.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;

    @PostMapping("/login")
    public Response.LoginResponse signIn(@RequestBody Request.LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Response.LoginResponse loginResponse = memberServiceImpl.login(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", loginResponse.getAccessToken(), loginResponse.getRefreshToken());
        return loginResponse;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response.SignUpResponse> signUp(@RequestBody Request.SignUpRequest signUpDto) {
        Response.SignUpResponse savedMemberDto = memberServiceImpl.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @GetMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

}
