package com.findthebusiness.backend.dto.shops;

import com.findthebusiness.backend.dto.categories.CategoriesDto;
import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;

import java.util.List;

public class ShopCardDto {

    private String id;
    private String name;
    private String smallPhoto;
    private Double rating;
    private Boolean isPromotedInHome;
    private Boolean isPromotedInSearches;
    private Integer promotedDaysInHomeRemaining = null;
    private Integer promotedDaysInSearchesRemaining = null;
    private String description;
    private String maximumSize;
    private Boolean isPublished;
    private List<SubcategoriesDto> subcategories;
    private CategoriesDto categories;

    public ShopCardDto() {
    }

    public ShopCardDto(String id, String name, String smallPhoto, Double rating, Boolean isPromotedInHome, Boolean isPromotedInSearches, Integer promotedDaysInHomeRemaining, Integer promotedDaysInSearchesRemaining, String description, String maximumSize, Boolean isPublished, List<SubcategoriesDto> subcategories, CategoriesDto categories) {
        this.id = id;
        this.name = name;
        this.smallPhoto = smallPhoto;
        this.rating = rating;
        this.isPromotedInHome = isPromotedInHome;
        this.isPromotedInSearches = isPromotedInSearches;
        this.promotedDaysInHomeRemaining = promotedDaysInHomeRemaining;
        this.promotedDaysInSearchesRemaining = promotedDaysInSearchesRemaining;
        this.description = description;
        this.maximumSize = maximumSize;
        this.isPublished = isPublished;
        this.subcategories = subcategories;
        this.categories = categories;
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

    public Boolean getPromotedInHome() {
        return isPromotedInHome;
    }

    public ShopCardDto setPromotedInHome(Boolean promotedInHome) {
        isPromotedInHome = promotedInHome;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ShopCardDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public CategoriesDto getCategories() {
        return categories;
    }

    public ShopCardDto setCategories(CategoriesDto categories) {
        this.categories = categories;
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

    public Boolean getPromotedInSearches() {
        return isPromotedInSearches;
    }

    public ShopCardDto setPromotedInSearches(Boolean promotedInSearches) {
        isPromotedInSearches = promotedInSearches;
        return this;
    }

    public Integer getPromotedDaysInHomeRemaining() {
        return promotedDaysInHomeRemaining;
    }

    public ShopCardDto setPromotedDaysInHomeRemaining(Integer promotedDaysInHomeRemaining) {
        this.promotedDaysInHomeRemaining = promotedDaysInHomeRemaining;
        return this;
    }

    public Integer getPromotedDaysInSearchesRemaining() {
        return promotedDaysInSearchesRemaining;
    }

    public ShopCardDto setPromotedDaysInSearchesRemaining(Integer promotedDaysInSearchesRemaining) {
        this.promotedDaysInSearchesRemaining = promotedDaysInSearchesRemaining;
        return this;
    }
}
