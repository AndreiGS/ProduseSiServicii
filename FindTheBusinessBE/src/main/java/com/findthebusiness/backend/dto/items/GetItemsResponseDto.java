package com.findthebusiness.backend.dto.items;


public class GetItemsResponseDto {

    private String id;
    private String photo;
    private String title;
    private String description;
    private String tabName;
    private String price;

    public GetItemsResponseDto() {
    }

    public GetItemsResponseDto(String id, String photo, String title, String description, String tabName, String price) {
        this.id = id;
        this.photo = photo;
        this.title = title;
        this.description = description;
        this.tabName = tabName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public GetItemsResponseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public GetItemsResponseDto setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public GetItemsResponseDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GetItemsResponseDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTabName() {
        return tabName;
    }

    public GetItemsResponseDto setTabName(String tabName) {
        this.tabName = tabName;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public GetItemsResponseDto setPrice(String price) {
        this.price = price;
        return this;
    }
}


