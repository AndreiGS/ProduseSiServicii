package com.findthebusiness.backend.dto.shops;

import java.util.List;

public class SaveShopRequestDto {

    private String photo;
    private String name;
    private String description;
    private List<String> subcategories;
    private String shopSize;
    private String base64SmallImage;

    public SaveShopRequestDto() {
    }

    public SaveShopRequestDto(String photo, String name, String description, List<String> subcategories, String shopSize, String base64SmallImage) {
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.subcategories = subcategories;
        this.shopSize = shopSize;
        this.base64SmallImage = base64SmallImage;
    }

    public String getBase64SmallImage() {
        return base64SmallImage;
    }

    public SaveShopRequestDto setBase64SmallImage(String base64SmallImage) {
        this.base64SmallImage = base64SmallImage;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public SaveShopRequestDto setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getName() {
        return name;
    }

    public SaveShopRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SaveShopRequestDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public SaveShopRequestDto setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
        return this;
    }

    public String getShopSize() {
        return shopSize;
    }

    public SaveShopRequestDto setShopSize(String shopSize) {
        this.shopSize = shopSize;
        return this;
    }
}
