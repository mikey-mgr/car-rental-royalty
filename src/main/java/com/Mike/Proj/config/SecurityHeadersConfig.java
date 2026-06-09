package com.Mike.Proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adds HTTP security headers to all responses.
 * Addresses SEC-011: Missing Production Browser Security Headers
 */
@Configuration
public class SecurityHeadersConfig {

    @Bean
    public OncePerRequestFilter securityHeadersFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException {
                
                // Content Security Policy - strict by default
                response.setHeader("Content-Security-Policy", 
                    "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; font-src 'self' data:;");
                
                // HTTP Strict Transport Security - enforce HTTPS
                response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
                
                // Prevent MIME type sniffing
                response.setHeader("X-Content-Type-Options", "nosniff");
                
                // Clickjacking protection
                response.setHeader("X-Frame-Options", "DENY");
                
                // XSS protection (older browsers)
                response.setHeader("X-XSS-Protection", "1; mode=block");
                
                // Referrer policy - limit referrer information leakage
                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                
                // Permissions policy - disable unnecessary APIs
                response.setHeader("Permissions-Policy", 
                    "geolocation=(), microphone=(), camera=(), usb=(), magnetometer=(), gyroscope=(), accelerometer=()");
                
                filterChain.doFilter(request, response);
            }
        };
    }
}
