package com.findthebusiness.backend.dto.search;

public class ShopDto {

    private String id;
    private String name;
    private String smallPhoto;
    private Double rating;
    private String type;
    private String description;

    public ShopDto() {
    }

    public ShopDto(String id, String name, String smallPhoto, Double rating, String type, String description) {
        this.id = id;
        this.name = name;
        this.smallPhoto = smallPhoto;
        this.rating = rating;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public ShopDto setType(String type) {
        this.type = type;
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
