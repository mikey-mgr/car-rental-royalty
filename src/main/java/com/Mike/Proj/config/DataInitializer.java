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
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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

        seedCategoriesAndProductsFromAssets();
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
            System.out.println("Asset-based category/product initialization skipped: " + ex.getMessage());
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
}
