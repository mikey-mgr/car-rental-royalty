package com.Mike.Proj.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;
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
        
        String allowedOrigins = System.getenv()
            .getOrDefault("ALLOWED_ORIGINS", "http://localhost:8081,http://localhost:8583");
        
        for (String origin : allowedOrigins.split(",")) {
            config.addAllowedOrigin(origin.trim());
        }
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-XSRF-TOKEN"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", config);

        return src;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) 
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
    }
    
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // CSRF is disabled because:
        // 1. All state-changing endpoints require authentication (authenticated() or hasRole("ADMIN"))
        // 2. CORS restricts which origins can interact with the API
        // 3. The XSRF-TOKEN cookie uses SameSite=Lax which isn't sent cross-origin for POST,
        //    breaking cart/wishlist on Render (frontend and backend are different domains)
        http.csrf(csrf -> csrf.disable())
            .cors()
            .and()
            .headers(headers -> headers
                // Security headers for production
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; img-src 'self' data: https:; font-src 'self' https://cdnjs.cloudflare.com;")
                )
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .preload(true)
                    .maxAgeInSeconds(31536000)
                )
                .frameOptions(frame -> frame.sameOrigin())
                .contentTypeOptions()
            )
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/health", "/healthz", "/product/list", "/product/find/**", "/category/list", "/category/show/**", "/user/signup", "/user/login-fail", "/user/api-login", "/user/csrf-token", "/contact/submit").permitAll()
                .requestMatchers("/user/signin", "/user/logout").permitAll()
                .requestMatchers("/cart/**", "/wishlist/**", "/order/create-checkout-session").authenticated()
                .anyRequest().hasRole("ADMIN"))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"status\":\"Fail\",\"message\":\"Unauthorized\"}");
                })
            )
            .formLogin()
                .defaultSuccessUrl("/user/signin", true)
                .usernameParameter("email")
                .permitAll()
            .and()
            .httpBasic()
            .and()
            .logout((logout) -> logout.logoutSuccessUrl("/user/logout"))
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()
            );

        return http.build();
    }
}