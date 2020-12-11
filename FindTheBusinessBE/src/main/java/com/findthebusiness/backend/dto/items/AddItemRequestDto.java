package com.findthebusiness.backend.dto.items;

public class AddItemRequestDto {

    private String title;
    private String description;
    private String tabName;
    private String newImage;
    private String price;

    public AddItemRequestDto(String title, String description, String tabName, String newImage, String price) {
        this.title = title;
        this.description = description;
        this.tabName = tabName;
        this.newImage = newImage;
        this.price = price;
    }

    public AddItemRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public AddItemRequestDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AddItemRequestDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTabName() {
        return tabName;
    }

    public AddItemRequestDto setTabName(String tabName) {
        this.tabName = tabName;
        return this;
    }

    public String getNewImage() {
        return newImage;
    }

    public AddItemRequestDto setNewImage(String newImage) {
        this.newImage = newImage;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public AddItemRequestDto setPrice(String price) {
        this.price = price;
        return this;
    }
}
