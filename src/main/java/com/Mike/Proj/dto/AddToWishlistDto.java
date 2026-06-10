package com.Mike.Proj.dto;

import jakarta.validation.constraints.NotNull;

public class AddToWishlistDto {
    private @NotNull Integer productId;

    public AddToWishlistDto() {
    }

    public AddToWishlistDto(Integer productId) {
        this.productId = productId;
    }

    /**
     * @return @NotNull Integer return the productId
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
}