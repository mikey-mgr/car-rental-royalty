package com.Mike.Proj.dto.cart;

import java.time.LocalDate;

import com.Mike.Proj.model.Cart;
import com.Mike.Proj.model.Product;

public class CartItemDto {
    private Integer id;
    private Integer userId;
    private Integer quantity;
    private Product product;
    private LocalDate bookedFor;
    private LocalDate bookedFrom;
    private String dropoffTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CartItemDto(Cart cart){
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.setProduct(cart.getProduct());
        this.setBookedFor(cart.getBookedFor());
        this.setBookedFrom(cart.getBookedFrom());
        this.setDropoffTime(cart.getDropoffTime());
        this.setUserId(cart.getUser().getId());
    }

    public LocalDate getBookedFor() {
        return bookedFor;
    }

    public void setBookedFor(LocalDate bookedFor) {
        this.bookedFor = bookedFor;
    }

    public LocalDate getBookedFrom() {
        return bookedFrom;
    }

    public void setBookedFrom(LocalDate bookedFrom) {
        this.bookedFrom = bookedFrom;
    }

    public String getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(String dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    /**
     * @return Integer return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}