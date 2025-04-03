package com.backend.legisloop.controller.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * Handle SpringBoot controller requests with the 'apiKey' security handler.
 */
public class ApiKeyAuthInterceptor implements HandlerInterceptor {

    private final String headerName;
    private final String expectedApiKey;

    public ApiKeyAuthInterceptor(String headerName, String expectedApiKey) {
        this.headerName = headerName;
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            // First, try to detect the Operation annotation on the method.
            Operation operation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Operation.class);
            boolean requiresApiKey = false;
            if (operation != null && operation.security() != null && operation.security().length > 0) {
                // Check if any of the security requirements has the name "apiKey"
                for (SecurityRequirement secReq : operation.security()) {
                    if ("apiKey".equals(secReq.name())) {
                        requiresApiKey = true;
                        break;
                    }
                }
            }

            if (requiresApiKey) {
                String requestApiKey = request.getHeader(headerName);
                if (requestApiKey == null || !Objects.equals(requestApiKey, expectedApiKey)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
            }
        }
        return true;
    }
}
