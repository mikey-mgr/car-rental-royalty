package com.Mike.Proj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Disables Spring Data REST auto-exposure of repositories (SEC-010).
 * Only explicitly configured endpoints are exposed via REST.
 */
@Configuration
public class RepositoryRestConfig implements RepositoryRestConfigurer {
    
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Disable Spring Data REST auto-exposure of all repositories
        // Only endpoints explicitly defined in controllers are exposed
        config.disableDefaultExposure();
    }
}
