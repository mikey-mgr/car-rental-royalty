package com.Mike.Proj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Mike.Proj.dto.WishlistDto;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.model.User;
import com.Mike.Proj.model.Wishlist;
import com.Mike.Proj.repository.WishlistRepo;

@Service
public class WishlistService {

    @Autowired
    WishlistRepo wishlistRepo;

    @Autowired
    ProductService productService;

    @SuppressWarnings("null")
    public void createWishlist(Wishlist wishlist) {
       wishlistRepo.save(wishlist);
    }

    //get a list of wishlists for user
    public List<WishlistDto> getWishlistForUser(User user) {
        final List<Wishlist> wishlists = wishlistRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<WishlistDto> wishlistDtos = new ArrayList<>();

        for(Wishlist wishlist: wishlists){
            WishlistDto list = new WishlistDto(wishlist);
            wishlistDtos.add(list);
        }
        return wishlistDtos;
    }

    @SuppressWarnings("null")
    public void deleteFromWishlist(Integer wishlistId, User user) {
        //check if wishlist exists
        Optional<Wishlist> optionalWishlist = wishlistRepo.findById(wishlistId);
        if(optionalWishlist.isEmpty()){
            throw new CustomException("Wishlist item is invalid: " + wishlistId);
        } 
        Wishlist wishlist = optionalWishlist.get();
        //check if item id belongs to user - use equals() not != for entity comparison
        if(!wishlist.getUser().getId().equals(user.getId())){
            throw new CustomException("Wishlist item does not belong to user: " + wishlistId);
        }
        wishlistRepo.delete(wishlist);
    }

    // public List<Wishlist> getWishlists(){
    //     wishlistRepo.findAllby();
    // }

}
