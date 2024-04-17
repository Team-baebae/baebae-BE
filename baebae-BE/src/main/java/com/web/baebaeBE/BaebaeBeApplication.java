package com.web.baebaeBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.web.baebaeBE.kakao.client")
public class BaebaeBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaebaeBeApplication.class, args);
	}

}
