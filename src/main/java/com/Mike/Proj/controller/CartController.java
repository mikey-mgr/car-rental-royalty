package com.Mike.Proj.controller;

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
import com.Mike.Proj.dto.cart.AddToCartDto;
import com.Mike.Proj.dto.cart.CartDto;
import com.Mike.Proj.model.User;
import com.Mike.Proj.repository.UserRepo;
import com.Mike.Proj.service.CartService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    UserRepo userRepo;

    //post cart api to add items
    @PostMapping("/add")
    public ResponseEntity<APIResponse> addToCart(@Valid @RequestBody AddToCartDto addToCartDto){
        //get user from Spring Security context
        User user = getUserFromContext();

        //add item to cart
        cartService.addToCart(addToCartDto, user);

        return new ResponseEntity<>(new APIResponse(true, "Added to cart"), HttpStatus.CREATED);

    }
    
    //get all cart items for a user
    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(){
        //get user from Spring Security context
        User user = getUserFromContext();
        
        //return cart item
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    //delete a cart item for a user
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<APIResponse> deleteCartitem(@PathVariable("cartItemId") Integer itemId){
        //get user from Spring Security context
        User user = getUserFromContext();

        //find the cart
        cartService.deleteFromCart(itemId, user);

        return new ResponseEntity<>(new APIResponse(true, "Item has been removed from cart"), HttpStatus.OK);
    }

    private User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email);
    }
}