package com.Mike.Proj.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Mike.Proj.common.APIResponse;
import com.Mike.Proj.dto.UserDto;
import com.Mike.Proj.dto.WishlistDto;
import com.Mike.Proj.dto.cart.CartDto;
import com.Mike.Proj.model.Category;
import com.Mike.Proj.repository.CategoryRepo;
import com.Mike.Proj.service.AdminService;
import com.Mike.Proj.service.AuthenticationService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CategoryRepo categoryRepo;

    //get a list of all users
    @GetMapping("/users/")
    public ResponseEntity<List<UserDto>> getUsers(){

        //find the user.
        // User user = authenticationService.getUser(token);

        //verify if user is an admin
        // adminService.verifyAdminUser(user);

        List<UserDto> userDto = adminService.getUsers();
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    //get all cart items
    @GetMapping("/all-cart-items/")
    public ResponseEntity<CartDto> getAllCartItems(){
        //find user
        // User user = authenticationService.getUser(token);

        //verify whether user is admin
        // adminService.verifyAdminUser(user);

        CartDto cartDto = adminService.listCartBookings();
        
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    //get all wishlists
    @GetMapping("/all-wishlists/")
    public ResponseEntity<List<WishlistDto>> getAllWishlists(){
        //find user
        // User user = authenticationService.getUser(token);

        //verify whether user is admin
        // adminService.verifyAdminUser(user);

        List<WishlistDto> wishlists = adminService.listWishlists();
        
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    @PostMapping("/seed-categories")
    public ResponseEntity<APIResponse> seedCategories() {
        List<Category> defaults = Arrays.asList(
            createSeedCat("Sedans", "Comfortable and fuel-efficient sedans perfect for city driving and business trips.", "/AppImages/sedan.png"),
            createSeedCat("SUVs", "Spacious SUVs for family trips and off-road adventures.", "/AppImages/suv.png"),
            createSeedCat("Hatchback", "Compact and easy-to-park hatchbacks ideal for urban commuting.", "/AppImages/hatchback.png"),
            createSeedCat("Offroad", "Heavy-duty offroad vehicles built for rough terrain.", "/AppImages/offroad.png"),
            createSeedCat("Coupe", "Sporty two-door coupes for a thrilling driving experience.", "/AppImages/coupe.png"),
            createSeedCat("Convertible", "Open-top convertibles for sunny day cruises.", "/AppImages/convertible.png")
        );

        int created = 0;
        for (Category cat : defaults) {
            if (categoryRepo.findByCategoryName(cat.getCategoryName()) == null) {
                categoryRepo.save(cat);
                created++;
            }
        }

        return new ResponseEntity<>(
            new APIResponse(true, created + " sample categor" + (created == 1 ? "y" : "ies") + " created."),
            HttpStatus.OK
        );
    }

    private Category createSeedCat(String name, String description, String imageUrl) {
        Category cat = new Category();
        cat.setCategoryName(name);
        cat.setDescription(description);
        cat.setImageUrl(imageUrl);
        return cat;
    }
}