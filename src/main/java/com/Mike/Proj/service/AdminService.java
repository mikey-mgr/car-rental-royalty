package com.Mike.Proj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mike.Proj.dto.UserDto;
import com.Mike.Proj.dto.WishlistDto;
import com.Mike.Proj.dto.cart.CartDto;
import com.Mike.Proj.dto.cart.CartItemDto;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.model.Cart;
import com.Mike.Proj.model.User;
import com.Mike.Proj.model.Wishlist;
import com.Mike.Proj.repository.CartRepo;
import com.Mike.Proj.repository.UserRepo;
import com.Mike.Proj.repository.WishlistRepo;

@Service
public class AdminService {
    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WishlistRepo wishlistRepo;

    //generate booking list for all users
    public CartDto listCartBookings() {
        List<Cart> cartList = cartRepo.findAll();
        
        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;

        if(!cartList.isEmpty()){
            for(Cart cart: cartList){
                CartItemDto cartItemDto = new CartItemDto(cart);
                totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
                cartItems.add(cartItemDto);
            }
        }
        
        CartDto cartDto = new CartDto();
        cartDto.setCartItems(cartItems);
        cartDto.setTotalCost(totalCost);

        return cartDto;
    }

    //delete cart item as admin
    @SuppressWarnings("null")
    public void deleteCartItem(Integer cartItemId, User user) {
        Optional<Cart> optionalCart = cartRepo.findById(cartItemId);
        //check if cart item exists
        if(optionalCart.isEmpty()){
            throw new CustomException("Cart item is invalid: " + cartItemId);
        }
        Cart cart = optionalCart.get();

        cartRepo.delete(cart); 
    }

    public void verifyAdminUser(User user) {
        if(user == null || !"ADMIN".equals(user.getRole())){
            throw new CustomException("You do not have admin privileges to access this information");
        }
    }

    //get a list of all users
    public List<UserDto> getUsers(){
        List<User> allUsers = userRepo.findAll();

        List<UserDto> users = new ArrayList<>();

        for(User user: allUsers){
            UserDto userDto = new UserDto(user);
            users.add(userDto);
        }
        return users;
    }

    //get a list of all wishlists
    public List<WishlistDto> listWishlists() {
        List<Wishlist> wishlists = wishlistRepo.findAll();
        List<WishlistDto> allWishlists = new ArrayList<>();
        
        if(!wishlists.isEmpty()){
            for(Wishlist list: wishlists){
                WishlistDto listDto = new WishlistDto(list);
                allWishlists.add(listDto);
            }
        }
        
        return allWishlists;
    }
}
