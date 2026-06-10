package com.Mike.Proj.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//this is the corsconfig class that actually works ie. for cross origin
@Configuration
public class MyCorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${ALLOWED_ORIGINS:http://localhost:8583}") String allowedOrigins) {
        return new WebMvcConfigurer() {
            @SuppressWarnings("null")
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedHeaders("Content-Type", "Authorization", "X-XSRF-TOKEN")
                        .allowCredentials(true);
            }
        };
    }
}