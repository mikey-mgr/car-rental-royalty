package com.Mike.Proj.dto.cart;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class AddToCartDto {
    private Integer id;
    private @NotNull Integer productId;
    private @NotNull Integer quantity;
    private @NotNull LocalDate bookedFor;
    private LocalDate bookedFrom;
    private String dropoffTime;


    public AddToCartDto(Integer productId, Integer quantity, LocalDate bookedFor) {
        this.productId = productId;
        this.quantity = quantity;
        this.bookedFor = bookedFor;
    }

    public AddToCartDto() {
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
     * @return @notNull Integer return the productId
     */
    public @NotNull Integer getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(@NotNull Integer productId) {
        this.productId = productId;
    }

    /**
     * @return @NotNull Integr return the quantity
     */
    public @NotNull Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(@NotNull Integer quantity) {
        this.quantity = quantity;
    }


    /**
     * @return @NotNull Date return the bookedFor
     */
    public @NotNull LocalDate getBookedFor() {
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
