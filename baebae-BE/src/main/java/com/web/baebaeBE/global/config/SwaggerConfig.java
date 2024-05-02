package com.web.baebaeBE.global.config;

import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;

// http://<서버주소>:<포트번호>/swagger-ui/index.html로 접속
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI publicApi() {
        Info info = new Info()
                .title("BeBe-Application")
                .description("BeBe-Application API 명세서")
                .version("v1");

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(HEADER)
                .bearerFormat("JWT")
                .scheme("Bearer");

        Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);

        return new OpenAPI()
                .info(info)
                .components(components);
    }
}