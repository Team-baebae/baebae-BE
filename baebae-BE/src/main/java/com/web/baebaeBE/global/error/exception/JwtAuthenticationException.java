package com.web.baebaeBE.global.error.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import com.web.baebaeBE.global.jwt.exception.TokenError;
import lombok.Getter;
import org.h2.command.Token;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private TokenError tokenError;

    public JwtAuthenticationException(TokenError tokenError) {
        super(tokenError.getMessage());
        this.tokenError = tokenError;
    }
}
