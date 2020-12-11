package com.findthebusiness.backend.dto.shops;

public class ReloadRatingAndPriceResponseDto {

    private Double rating;
    private Double price;

    public ReloadRatingAndPriceResponseDto() {
    }

    public ReloadRatingAndPriceResponseDto(Double rating, Double price) {
        this.rating = rating;
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public ReloadRatingAndPriceResponseDto setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public ReloadRatingAndPriceResponseDto setPrice(Double price) {
        this.price = price;
        return this;
    }
}
