package com.Mike.Proj.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class CartHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "user_name")
    private String user;

    @Column(name = "car_name")
    private String product;

    @Column(name = "booked_days")
    private int quantity;

    @Column(name = "booked_for")
    private LocalDate bookedFor;

    @Column(name = "booked_from")
    private LocalDate bookedFrom;

    @Column(name = "dropoff_time")
    private String dropoffTime;
    
    private Double price;

    public CartHistory(Integer cartId, Date createdDate, String user, String product, int quantity,
            LocalDate bookedFor, LocalDate bookedFrom, String dropoffTime, Double price) {
        this.cartId = cartId;
        this.createdDate = createdDate;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.bookedFor = bookedFor;
        this.bookedFrom = bookedFrom;
        this.dropoffTime = dropoffTime;
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return String return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

}
