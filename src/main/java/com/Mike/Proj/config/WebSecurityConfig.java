package com.Mike.Proj.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    CorsConfigurationSource corsConfigSrc(){
        CorsConfiguration config = new CorsConfiguration();
        
        // Get allowed origins from environment variable
        String allowedOrigins = System.getenv()
            .getOrDefault("ALLOWED_ORIGINS", "http://localhost:8583");
        
        for (String origin : allowedOrigins.split(",")) {
            config.addAllowedOrigin(origin.trim());
        }
        
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // CRITICAL: Allow cookies
        
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", config);

        return src;
    }
    
    @SuppressWarnings("removal")
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors()
            .and()
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/product/list", "/product/find/**", "/category/list", "/category/show/**", "/user/**", "/contact/submit").permitAll()
                .requestMatchers("/cart/**", "/wishlist/**", "/order/create-checkout-session").authenticated()
                .anyRequest().hasRole("ADMIN"))
            .formLogin()
                .defaultSuccessUrl("/user/signin", true)
                .usernameParameter("email")
                .permitAll()
            .and()
            .httpBasic()
            .and()
            .logout((logout) -> logout.logoutSuccessUrl("/user/logout"))
            .sessionManagement(session -> session
                .sessionFixation().none()
            );

        return http.build();
    }
}