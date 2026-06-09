package com.Mike.Proj.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "car")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private @NotNull String name;
    @Column(length = 10000)
    private @NotNull String imageURL;
    @Positive(message = "Price must be greater than 0")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private @NotNull double price;
    @Column(length = 1000)
    private @NotNull String description;
    private @NotNull String bookingStatus;
    @Column(length = 1000)
    private @NotNull ArrayList<String> features;
    @Column(length = 10000)
    private @NotNull ArrayList<String> carousel_imgs;

    public Product(){
        
    }

    public Product(ArrayList<String> features, ArrayList<String> imgs) {
        this.name = "string";
        this.imageURL = "string";
        this.price = 50;
        this.description = "string";
        this.bookingStatus = "string";
        this.features = features;
        this.carousel_imgs = imgs;
    }

    // Many to one relationship
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return @NotNull String return the bookingStatus
     */
    public @NotNull String getBookingStatus() {
        return bookingStatus;
    }

    /**
     * @param bookingStatus the bookingStatus to set
     */
    public void setBookingStatus(@NotNull String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }


    /**
     * @return @NotNull ArrayList<String> return the features
     */
    public ArrayList<String> getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }


    /**
     * @return ArrayList<String> return the carousel_imgs
     */
    public ArrayList<String> getCarousel_imgs() {
        return carousel_imgs;
    }

    /**
     * @param carousel_imgs the carousel_imgs to set
     */
    public void setCarousel_imgs(ArrayList<String> carousel_imgs) {
        this.carousel_imgs = carousel_imgs;
    }
}