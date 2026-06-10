package com.Mike.Proj.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Mike.Proj.common.APIResponse;
import com.Mike.Proj.dto.UserDto;
import com.Mike.Proj.dto.WishlistDto;
import com.Mike.Proj.dto.cart.CartDto;
import com.Mike.Proj.service.AdminService;
import com.Mike.Proj.service.AuthenticationService;
import com.Mike.Proj.service.PinataService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PinataService pinataService;

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

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("Upload request received: fileName={}, size={}, contentType={}",
            file.getOriginalFilename(), file.getSize(), file.getContentType());
        try {
            String url = pinataService.uploadImage(file);
            log.info("Upload successful: {}", url);
            return new ResponseEntity<>(new APIResponse(true, url), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn("Upload validation failed: {}", e.getMessage());
            return new ResponseEntity<>(new APIResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Upload failed", e);
            return new ResponseEntity<>(new APIResponse(false, "Upload failed: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}