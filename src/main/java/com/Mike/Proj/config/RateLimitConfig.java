package com.Mike.Proj.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiting configuration to prevent abuse.
 * Addresses SEC-008: Missing Rate Limits and Abuse Controls
 * 
 * Note: For production, consider using a distributed cache (Redis) instead of in-memory storage.
 */
@Configuration
public class RateLimitConfig {

    private static final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private static final int REQUESTS_PER_MINUTE = 60;
    private static final int LOGIN_REQUESTS_PER_MINUTE = 5;
    private static final int CONTACT_REQUESTS_PER_MINUTE = 3;

    @Bean
    public OncePerRequestFilter rateLimitFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException {
                
                String path = request.getRequestURI();
                String clientIp = getClientIp(request);
                String bucketKey = clientIp + ":" + path;
                
                Bucket bucket = buckets.computeIfAbsent(bucketKey, k -> createBucket(path));
                
                if (bucket.tryConsume(1)) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(429); // Too Many Requests
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Rate limit exceeded. Please try again later.\"}");
                }
            }

            private Bucket createBucket(String path) {
                int requestsPerMinute = REQUESTS_PER_MINUTE;
                
                // Tighter limits on sensitive endpoints
                if (path.contains("/user/login") || path.contains("/user/signin")) {
                    requestsPerMinute = LOGIN_REQUESTS_PER_MINUTE;
                } else if (path.contains("/contact/submit")) {
                    requestsPerMinute = CONTACT_REQUESTS_PER_MINUTE;
                }
                
                Bandwidth limit = Bandwidth.classic(requestsPerMinute, Refill.intervally(requestsPerMinute, Duration.ofMinutes(1)));
                return Bucket4j.builder().addLimit(limit).build();
            }

            private String getClientIp(HttpServletRequest request) {
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        };
    }
}
