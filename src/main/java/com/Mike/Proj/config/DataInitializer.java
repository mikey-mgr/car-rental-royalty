package com.Mike.Proj.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.Mike.Proj.model.User;
import com.Mike.Proj.repository.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Bootstrap admin account from environment variables if configured
        bootstrapAdminUser();
    }

    /**
     * Securely bootstrap the initial admin user from environment variables.
     * 
     * To enable admin user creation, set these environment variables:
     * - ADMIN_BOOTSTRAP_ENABLED=true
     * - ADMIN_EMAIL=admin@yourcompany.com
     * - ADMIN_PASSWORD=<strong-password>
     * 
     * This approach follows security best practices:
     * 1. Admin creation is opt-in via ADMIN_BOOTSTRAP_ENABLED flag
     * 2. Credentials are provided via environment variables (stored in secret manager in prod)
     * 3. Admin user is only created if it doesn't already exist
     * 4. Warnings are logged when bootstrap is disabled or admin already exists
     * 
     * For Docker/Render deployments:
     * - Define environment variables in Render's Environment section
     * - Use SecureString type for sensitive values
     * - Environment variables are injected at container runtime
     */
    private void bootstrapAdminUser() {
        String bootstrapEnabled = System.getenv("ADMIN_BOOTSTRAP_ENABLED");
        
        // Only proceed if explicitly enabled
        if (bootstrapEnabled == null || !bootstrapEnabled.equalsIgnoreCase("true")) {
            LOGGER.info("Admin bootstrap disabled. To enable, set ADMIN_BOOTSTRAP_ENABLED=true");
            return;
        }
        
        String adminEmail = System.getenv("ADMIN_EMAIL");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        
        // Validate required environment variables
        if (adminEmail == null || adminEmail.trim().isEmpty()) {
            LOGGER.warn("ADMIN_BOOTSTRAP_ENABLED is true, but ADMIN_EMAIL is not set. Skipping admin creation.");
            return;
        }
        if (adminPassword == null || adminPassword.trim().isEmpty()) {
            LOGGER.warn("ADMIN_BOOTSTRAP_ENABLED is true, but ADMIN_PASSWORD is not set. Skipping admin creation.");
            return;
        }
        
        // Check if admin user already exists
        if (userRepo.findByEmail(adminEmail) != null) {
            LOGGER.info("Admin user with email {} already exists. Skipping creation.", adminEmail);
            return;
        }
        
        try {
            // Create new admin user
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setFirstName("System");
            adminUser.setLastName("Admin");
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole("ADMIN");
            
            userRepo.save(adminUser);
            LOGGER.info("Admin user successfully created with email: {}", adminEmail);
        } catch (Exception e) {
            LOGGER.error("Failed to create admin user: {}", e.getMessage(), e);
        }
    }

}
