package com.findthebusiness.backend.dto.shops;

import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;

import java.util.List;

public class ShopCardDto {

    private String id;
    private String name;
    private String smallPhoto;
    private Double rating;
    private String type;
    private Integer promotedDaysRemaining = null;
    private String description;
    private String maximumSize;
    private Boolean isPublished;
    private List<SubcategoriesDto> subcategories;

    public ShopCardDto() {
    }

    public String getId() {
        return id;
    }

    public ShopCardDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ShopCardDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getSmallPhoto() {
        return smallPhoto;
    }

    public ShopCardDto setSmallPhoto(String photo) {
        this.smallPhoto = photo;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public ShopCardDto setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getType() {
        return type;
    }

    public ShopCardDto setType(String type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ShopCardDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<SubcategoriesDto> getSubcategories() {
        return subcategories;
    }

    public ShopCardDto setSubcategories(List<SubcategoriesDto> subcategories) {
        this.subcategories = subcategories;
        return this;
    }

    public String getMaximumSize() {
        return maximumSize;
    }

    public ShopCardDto setMaximumSize(String maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public ShopCardDto setPublished(Boolean published) {
        isPublished = published;
        return this;
    }

    public Integer getPromotedDaysRemaining() {
        return promotedDaysRemaining;
    }

    public ShopCardDto setPromotedDaysRemaining(Integer promotedDaysRemaining) {
        this.promotedDaysRemaining = promotedDaysRemaining;
        return this;
    }
}
