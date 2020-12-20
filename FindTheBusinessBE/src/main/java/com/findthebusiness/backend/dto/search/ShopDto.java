package com.findthebusiness.backend.dto.search;

public class ShopDto {

    private String id;
    private String name;
    private String smallPhoto;
    private Double rating;
    private Boolean isPromotedInSearches;
    private String description;

    public ShopDto() {
    }

    public ShopDto(String id, String name, String smallPhoto, Double rating, Boolean isPromotedInSearches, String description) {
        this.id = id;
        this.name = name;
        this.smallPhoto = smallPhoto;
        this.rating = rating;
        this.isPromotedInSearches = isPromotedInSearches;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public ShopDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ShopDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getSmallPhoto() {
        return smallPhoto;
    }

    public ShopDto setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public ShopDto setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Boolean getPromotedInSearches() {
        return isPromotedInSearches;
    }

    public ShopDto setPromotedInSearches(Boolean promotedInSearches) {
        isPromotedInSearches = promotedInSearches;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ShopDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
