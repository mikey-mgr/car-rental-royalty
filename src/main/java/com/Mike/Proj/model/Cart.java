package com.Mike.Proj.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "bookings")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Product product;

    @Column(name = "booked_days")
    private int quantity;

    @Column(name = "booked_for")
    private LocalDate bookedFor;

    @Column(name = "booked_from")
    private LocalDate bookedFrom;

    @Column(name = "dropoff_time")
    private String dropoffTime;

    public Cart(Date createdDate, User user, int quantity, LocalDate bookedFor) {
        this.createdDate = createdDate;
        this.user = user;
        this.quantity = quantity;
        this.bookedFor = bookedFor;
    }

    public Cart() {
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
     * @return Date return the date
     */
    public Date getDate() {
        return createdDate;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return User return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return int return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return Product return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return Date return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return Date return the bookedFor
     */
    public LocalDate getBookedFor() {
        return bookedFor;
    }

    /**
     * @param bookedFor the bookedFor to set
     */
    public void setBookedFor(@NotNull LocalDate bookedFor) {
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

}
