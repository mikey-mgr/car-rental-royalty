package com.Mike.Proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Mike.Proj.dto.UserDto;
import com.Mike.Proj.dto.WishlistDto;
import com.Mike.Proj.dto.cart.CartDto;
import com.Mike.Proj.service.AdminService;
import com.Mike.Proj.service.AuthenticationService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationService authenticationService;

    //get a list of all users
    @GetMapping("/users/")
    public ResponseEntity<List<UserDto>> getUsers(){

        //find the user
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
}