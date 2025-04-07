package com.gdg.planpal.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("planpal REST API")
                        .version("1.0")
                        .description("planpal REST API 명세서입니다."));
    }

    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/api/**"};
        String[] packagesToScan = {"com.gdg.planpal.domain"};
        return GroupedOpenApi.builder()
                .group("planpal-openapi")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }
}
