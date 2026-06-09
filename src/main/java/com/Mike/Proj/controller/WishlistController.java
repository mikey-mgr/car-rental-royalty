package com.Mike.Proj.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Mike.Proj.common.APIResponse;
import com.Mike.Proj.dto.WishlistDto;
import com.Mike.Proj.model.Product;
import com.Mike.Proj.model.User;
import com.Mike.Proj.model.Wishlist;
import com.Mike.Proj.repository.UserRepo;
import com.Mike.Proj.service.WishlistService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    
    @Autowired
    WishlistService wishlistService;

    @Autowired
    UserRepo userRepo;

    //save products as wishlist item
    @PostMapping("/add")
    public ResponseEntity<APIResponse> addToWishlist(@Valid @RequestBody Product product){
        
        //get user from security context
        User user = getUserFromContext();

        //save the item in wishlist
        Wishlist wishlist = new Wishlist(user, product);
        wishlistService.createWishlist(wishlist);

        APIResponse apiResponse = new APIResponse(true, "Added to wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // get all wishlist items for a user
    @GetMapping("/")
    public ResponseEntity<List<WishlistDto>> getWishlist(){
        
        //get user from security context
        User user = getUserFromContext();

        List<WishlistDto> wishlistDtos = wishlistService.getWishlistForUser(user);
        return new ResponseEntity<List<WishlistDto>>(wishlistDtos, HttpStatus.OK);

    }

    //delete a wishlist item for a user
    @DeleteMapping("/delete/{wishlistId}")
    public ResponseEntity<APIResponse> deleteWishlist(@PathVariable("wishlistId") Integer wishlistId){
        
        //get user from security context
        User user = getUserFromContext();

        //delete the item from wishlist
        wishlistService.deleteFromWishlist(wishlistId, user);

        return new ResponseEntity<>(new APIResponse(true, "Item has been removed from wishlist"), HttpStatus.OK);
    }

    private User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email);
    }
}