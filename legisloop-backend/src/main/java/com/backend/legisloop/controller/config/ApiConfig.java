package com.backend.legisloop.controller.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

	@Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                // "apiKey" is the scheme name we reference in @SecurityRequirement.
                .addSecuritySchemes("apiKey", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-API-KEY")));
    }
}

