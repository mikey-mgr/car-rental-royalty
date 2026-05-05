package com.Mike.Proj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.Mike.Proj.model.AuthenticationToken;
import com.Mike.Proj.model.User;
import com.Mike.Proj.repository.TokenRepository;
import com.Mike.Proj.repository.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        User existingAdmin = userRepo.findByEmail("admin@carrental.com");
        
        if (existingAdmin == null) {
            // Create default admin user
            String encryptedPassword = passwordEncoder.encode("admin123");
            User adminUser = new User(
                "Admin",
                "User",
                "admin@carrental.com",
                encryptedPassword,
                "ADMIN"
            );
            
            userRepo.save(adminUser);
            
            // Create authentication token for admin
            AuthenticationToken authToken = new AuthenticationToken(adminUser);
            tokenRepo.save(authToken);
            
            System.out.println("========================================");
            System.out.println("Default Admin Account Created:");
            System.out.println("Email: admin@carrental.com");
            System.out.println("Password: admin123");
            System.out.println("========================================");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
