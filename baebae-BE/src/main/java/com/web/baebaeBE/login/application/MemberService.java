package com.web.baebaeBE.login.application;

import com.web.baebaeBE.login.dto.Request;
import com.web.baebaeBE.login.dto.Response;

public interface MemberService {
    Response.LoginResponse login(String username, String password);

    Response.SignUpResponse signUp(Request.SignUpRequest signUpRequest);
}