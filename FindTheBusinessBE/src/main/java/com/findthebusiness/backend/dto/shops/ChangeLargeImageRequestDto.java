package com.findthebusiness.backend.dto.shops;

public class ChangeLargeImageRequestDto {

    private String newImage;

    public ChangeLargeImageRequestDto() {
    }

    public ChangeLargeImageRequestDto(String newImage) {
        this.newImage = newImage;
    }

    public String getNewImage() {
        return newImage;
    }

    public ChangeLargeImageRequestDto setNewImage(String newImage) {
        this.newImage = newImage;
        return this;
    }
}
