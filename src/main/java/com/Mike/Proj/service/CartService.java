package com.Mike.Proj.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Mike.Proj.dto.cart.AddToCartDto;
import com.Mike.Proj.dto.cart.CartDto;
import com.Mike.Proj.dto.cart.CartItemDto;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.model.Cart;
import com.Mike.Proj.model.CartHistory;
import com.Mike.Proj.model.Product;
import com.Mike.Proj.model.User;
import com.Mike.Proj.repository.CartHistoryRepo;
import com.Mike.Proj.repository.CartRepo;

@Service
public class CartService{
    
    @Autowired
    ProductService productService;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartHistoryRepo cartHistoryRepo;

    //add new item to cart
    public void addToCart(AddToCartDto addToCartDto, User user) {

        //find product by id
        Product product = productService.findById(addToCartDto.getProductId());

        // save cart item
        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setUser(user);
        cart.setDate(new Date());
        cart.setBookedFor(addToCartDto.getBookedFor());

        cartRepo.save(cart);

        // (Integer cartId, Date createdDate, String user, String product, int quantity, LocalDate bookedFor)
        String username = user.getFirstName() + " " + user.getLastName();

        Double price = product.getPrice() * addToCartDto.getQuantity();

        CartHistory cartHistory = new CartHistory(cart.getId(), new Date(), username, product.getName(), addToCartDto.getQuantity(), addToCartDto.getBookedFor(), price);

        cartHistoryRepo.save(cartHistory);
    }

    //generate booking list sorted according to date created
    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepo.findAllByUserOrderByCreatedDateDesc(user);
        
        List<CartItemDto> cartItems = new ArrayList<>();

        double totalCost = 0;
        for(Cart cart: cartList){
            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }
        CartDto cartDto = new CartDto();
        cartDto.setCartItems(cartItems);
        cartDto.setTotalCost(totalCost);
        return cartDto;
    }

    //delete booking from cart
    @SuppressWarnings("null")
    public void deleteFromCart(Integer cartItemId, User user) {
        //check if item exists
        Optional<Cart> optionalCart = cartRepo.findById(cartItemId);
        if(optionalCart.isEmpty()){
            throw new CustomException("Cart item is invalid: " + cartItemId);
        } 
        Cart cart = optionalCart.get();
        //check if item id belongs to user - use equals() not != for entity comparison
        if(!cart.getUser().getId().equals(user.getId())){
            throw new CustomException("Cart item does not belong to user: " + cartItemId);
        }
        cartRepo.delete(cart);  
    }
}