package com.findthebusiness.backend.dto.items;

import com.findthebusiness.backend.dto.tabs.TabResponseDto;
import com.findthebusiness.backend.dto.tabs.TabWithoutIdDto;

public class ItemResponseDto {

    private String id;
    private String title;
    private String description;
    private String photo;
    private String price;
    private TabWithoutIdDto tabWithoutIdDto;

    public ItemResponseDto() {
    }

    public ItemResponseDto(String id, String title, String description, String photo, String price, TabWithoutIdDto tabWithoutIdDto) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.tabWithoutIdDto = tabWithoutIdDto;
    }

    public String getId() {
        return id;
    }

    public ItemResponseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ItemResponseDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemResponseDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public ItemResponseDto setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public TabWithoutIdDto getTabWithoutIdDto() {
        return tabWithoutIdDto;
    }

    public ItemResponseDto setTabWithoutIdDto(TabWithoutIdDto tabWithoutIdDto) {
        this.tabWithoutIdDto = tabWithoutIdDto;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public ItemResponseDto setPrice(String price) {
        this.price = price;
        return this;
    }
}
