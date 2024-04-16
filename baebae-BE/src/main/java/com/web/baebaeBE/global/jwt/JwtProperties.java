package com.web.baebaeBE.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // application.yml에서 존재하는 jwt 설정을 가지고 옴.
public class JwtProperties {

    private String issuer; // 아이디
    private String secretKey; //Secret-Key
}

