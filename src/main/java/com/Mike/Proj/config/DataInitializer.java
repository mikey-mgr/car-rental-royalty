package com.Mike.Proj.config;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Mike.Proj.model.AuthenticationToken;
import com.Mike.Proj.model.Category;
import com.Mike.Proj.model.Product;
import com.Mike.Proj.model.User;
import com.Mike.Proj.repository.CategoryRepo;
import com.Mike.Proj.repository.ProductRepo;
import com.Mike.Proj.repository.TokenRepository;
import com.Mike.Proj.repository.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public void run(String... args) throws Exception {
        // Bootstrap admin account from environment variables if configured
        bootstrapAdminUser();
        
        // Seed categories and products from assets folder
        seedCategoriesAndProductsFromAssets();
        
        // Seed predefined vehicle catalog from JSON data
        seedVehiclesFromData();
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

    private void seedCategoriesAndProductsFromAssets() {
        try {
            Path productsRoot = Paths.get("src", "assets", "AppImages", "products");
            if (!Files.exists(productsRoot) || !Files.isDirectory(productsRoot)) {
                return;
            }

            Map<String, Category> existingCategories = categoryRepo.findAll().stream()
                .collect(Collectors.toMap(c -> normalize(c.getCategoryName()), c -> c, (a, b) -> a, HashMap::new));

            Map<String, Product> existingProducts = new HashMap<>();
            for (Product product : productRepo.findAll()) {
                if (product.getCategory() != null && product.getName() != null) {
                    existingProducts.put(productKey(product.getCategory().getId(), product.getName()), product);
                }
            }

            List<Path> categoryDirs;
            try (Stream<Path> stream = Files.list(productsRoot)) {
                categoryDirs = stream
                    .filter(Files::isDirectory)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
            }

            for (Path categoryDir : categoryDirs) {
                List<Path> productDirs = listPopulatedProductDirs(categoryDir);
                if (productDirs.isEmpty()) {
                    // Empty category folders are not initialized.
                    continue;
                }

                String categoryName = categoryDir.getFileName().toString().trim();
                if (categoryName.isEmpty()) {
                    continue;
                }

                Category category = existingCategories.get(normalize(categoryName));
                if (category == null) {
                    String categoryImage = findCategoryImage(categoryDir, productDirs);
                    if (categoryImage == null) {
                        // Skip category if there is no usable image in category root or product folders.
                        continue;
                    }

                    Category newCategory = new Category();
                    newCategory.setCategoryName(categoryName);
                    newCategory.setDescription("Auto-generated from assets folder: " + categoryName);
                    newCategory.setImageUrl(categoryImage);
                    category = categoryRepo.save(newCategory);
                    existingCategories.put(normalize(categoryName), category);
                }

                for (Path productDir : productDirs) {
                    String productName = productDir.getFileName().toString().trim();
                    if (productName.isEmpty()) {
                        continue;
                    }

                    String productKey = productKey(category.getId(), productName);
                    if (existingProducts.containsKey(productKey)) {
                        // Do not modify existing products in DB.
                        continue;
                    }

                    List<Path> images = listImageFiles(productDir);
                    if (images.isEmpty()) {
                        // Empty product folders are not initialized.
                        continue;
                    }

                    Product product = new Product();
                    product.setCategory(category);
                    product.setName(productName);
                    product.setImageURL(toFrontendAssetPath(images.get(0)));
                    product.setPrice(150.0);
                    product.setDescription("Auto-generated from assets folder: " + productName);
                    product.setBookingStatus("Available");
                    product.setFeatures(defaultFeatures());
                    product.setCarousel_imgs(images.stream().map(this::toFrontendAssetPath)
                        .collect(Collectors.toCollection(ArrayList::new)));

                    Product saved = productRepo.save(product);
                    existingProducts.put(productKey, saved);
                }
            }
        } catch (Exception ex) {
            LOGGER.warn("Asset-based category/product initialization skipped.");
        }
    }

    private List<Path> listPopulatedProductDirs(Path categoryDir) throws Exception {
        try (Stream<Path> stream = Files.list(categoryDir)) {
            return stream
                .filter(Files::isDirectory)
                .filter(path -> {
                    try {
                        return !listImageFiles(path).isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        }
    }

    private List<Path> listImageFiles(Path folder) throws Exception {
        try (Stream<Path> stream = Files.list(folder)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(this::isImageFile)
                .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        }
    }

    private boolean isImageFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
            || fileName.endsWith(".webp") || fileName.endsWith(".gif") || fileName.endsWith(".svg");
    }

    private String findCategoryImage(Path categoryDir, List<Path> productDirs) throws Exception {
        List<Path> categoryLevelImages = listImageFiles(categoryDir);
        if (!categoryLevelImages.isEmpty()) {
            return toFrontendAssetPath(categoryLevelImages.get(0));
        }

        // If category folder has no image, fallback to first image from populated product folders.
        for (Path productDir : productDirs) {
            List<Path> productImages = listImageFiles(productDir);
            if (!productImages.isEmpty()) {
                return toFrontendAssetPath(productImages.get(0));
            }
        }
        return null;
    }

    private String toFrontendAssetPath(Path imagePath) {
        Path appImagesRoot = Paths.get("src", "assets", "AppImages").toAbsolutePath().normalize();
        Path absoluteImage = imagePath.toAbsolutePath().normalize();
        Path relative = appImagesRoot.relativize(absoluteImage);

        // Vue CLI exposes copied AppImages at /AppImages/... (see vue.config.js), not /assets/
        StringBuilder pathBuilder = new StringBuilder("/AppImages/");
        for (Path part : relative) {
            if (pathBuilder.charAt(pathBuilder.length() - 1) != '/') {
                pathBuilder.append('/');
            }
            pathBuilder.append(encodePathSegment(part.toString()));
        }
        return pathBuilder.toString();
    }

    private String encodePathSegment(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private ArrayList<String> defaultFeatures() {
        ArrayList<String> features = new ArrayList<>();
        features.add("Air Conditioning");
        features.add("Power Steering");
        features.add("Bluetooth Connectivity");
        return features;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String productKey(Integer categoryId, String productName) {
        return categoryId + "::" + normalize(productName);
    }

    /**
     * Seed predefined vehicle catalog from vehicles-seed-data.json
     * Inserts rental vehicles with pricing and descriptions
     */
    private void seedVehiclesFromData() {
        try {
            // Load vehicles from classpath resource
            ClassPathResource resource = new ClassPathResource("vehicles-seed-data.json");
            if (!resource.exists()) {
                LOGGER.info("vehicles-seed-data.json not found, skipping vehicle seeding.");
                return;
            }

            String jsonContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            List<VehicleData> vehicles = mapper.readValue(jsonContent, 
                new TypeReference<List<VehicleData>>() {});

            if (vehicles == null || vehicles.isEmpty()) {
                LOGGER.warn("vehicles-seed-data.json is empty, skipping vehicle seeding.");
                return;
            }

            LOGGER.info("Found {} vehicles in seed data", vehicles.size());

            Map<String, Category> categories = new HashMap<>();
            for (Category cat : categoryRepo.findAll()) {
                String normalizedName = normalize(cat.getCategoryName());
                categories.put(normalizedName, cat);
                LOGGER.debug("Mapped category: {} -> {}", cat.getCategoryName(), normalizedName);
            }

            int vehiclesCreated = 0;
            for (VehicleData vehicleData : vehicles) {
                try {
                    if (vehicleData.getName() == null || vehicleData.getName().trim().isEmpty()) {
                        LOGGER.debug("Skipping vehicle with null/empty name");
                        continue;
                    }

                    // Find or create category
                    String categoryName = vehicleData.getCategoryName();
                    if (categoryName == null || categoryName.trim().isEmpty()) {
                        LOGGER.debug("Skipping vehicle {} with null/empty category", vehicleData.getName());
                        continue;
                    }

                    String normalizedCategory = normalize(categoryName);
                    Category category = categories.get(normalizedCategory);
                    LOGGER.debug("Looking for category: '{}' (normalized: '{}') - found: {}", categoryName, normalizedCategory, category != null);
                    
                    if (category == null) {
                        category = new Category();
                        category.setCategoryName(categoryName);
                        category.setDescription("Auto-generated from vehicle data: " + categoryName);
                        category.setImageUrl("/AppImages/default-category.png");
                        category = categoryRepo.save(category);
                        categories.put(normalize(categoryName), category);
                        LOGGER.info("Created new category: {}", categoryName);
                    }

                    // Build unique vehicle name with year
                    String vehicleName = vehicleData.getName() + " " + vehicleData.getYear();

                    // Check if vehicle already exists
                    List<Product> existing = productRepo.findAll().stream()
                        .filter(p -> p.getName().equalsIgnoreCase(vehicleName))
                        .collect(Collectors.toList());
                    
                    if (!existing.isEmpty()) {
                        LOGGER.debug("Vehicle already exists: {}", vehicleName);
                        continue; // Skip if already exists
                    }

                    // Create product
                    Product product = new Product();
                    product.setCategory(category);
                    product.setName(vehicleName);
                    product.setPrice(vehicleData.getPrice());
                    product.setDescription(vehicleData.getDescription());
                    product.setBookingStatus("Available");
                    product.setImageURL("/AppImages/default-vehicle.png");
                    
                    // Set features, handle null case
                    List<String> features = vehicleData.getFeatures();
                    ArrayList<String> featuresList = new ArrayList<>();
                    if (features != null) {
                        featuresList.addAll(features);
                    }
                    product.setFeatures(featuresList);
                    
                    // Set carousel images based on features count
                    ArrayList<String> carouselImgs = new ArrayList<>();
                    if (features != null) {
                        for (int i = 0; i < features.size(); i++) {
                            carouselImgs.add("/AppImages/default-vehicle.png");
                        }
                    } else {
                        carouselImgs.add("/AppImages/default-vehicle.png");
                    }
                    product.setCarousel_imgs(carouselImgs);
                    
                    Product savedProduct = productRepo.save(product);
                    vehiclesCreated++;
                    LOGGER.info("Created vehicle: {} (ID: {}) @ ${}", vehicleName, savedProduct.getId(), vehicleData.getPrice());
                    
                } catch (Exception e) {
                    LOGGER.error("Failed to seed vehicle {}: {}", vehicleData.getName(), e.getMessage());
                }
            }
            
            LOGGER.info("Vehicle catalog seeding completed. {} vehicles created from vehicles-seed-data.json", vehiclesCreated);
        } catch (Exception ex) {
            LOGGER.error("Vehicle seeding failed with error: {}", ex.getMessage(), ex);
        }
    }

    /**
     * Data transfer object for vehicle seed data
     */
    private static class VehicleData {
        private String name;
        private String year;
        private String color;
        private double price;
        private String description;
        private List<String> features;
        private String categoryName;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public List<String> getFeatures() { return features; }
        public void setFeatures(List<String> features) { this.features = features; }
        
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    }
}
