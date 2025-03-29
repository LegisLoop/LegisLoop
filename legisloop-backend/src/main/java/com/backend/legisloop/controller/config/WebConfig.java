package com.backend.legisloop.controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Load the API key (acting as your password) from application.properties.
    @Value("${legisloop.api.key}")
    private String apiKey;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // The interceptor is added for all endpoints, but it will only enforce API key authentication for endpoints annotated with @SecurityRequirement.
        registry.addInterceptor(new ApiKeyAuthInterceptor("X-API-KEY", apiKey));
    }
}
