package com.example.SentiStock_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SentiStock API 문서")
                        .description("감정 기반 주식 분석 서비스의 백엔드 API 명세서입니다.")
                        .version("v1.0.0"));
    }
}
