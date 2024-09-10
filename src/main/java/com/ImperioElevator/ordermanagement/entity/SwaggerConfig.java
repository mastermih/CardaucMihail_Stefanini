package com.ImperioElevator.ordermanagement.entity;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")  // Group name for APIs
                .pathsToMatch("/**")  // Define the path patterns to include all APIs
                .packagesToExclude("com.yourpackage.model")  // Exclude specific packages
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Imerior").version("API version: 1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
//                .components(new Components()  // I have no Spring security by the moment
//                        .addSecuritySchemes("bearerAuth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")));
    }
}

