package com.findthebusiness.backend.dto.shops;

public class ChangeStorefrontImageRequestDto {

    private String newImage;
    private String name;
    private String description;
    private String schedule;

    public ChangeStorefrontImageRequestDto() {
    }

    public ChangeStorefrontImageRequestDto(String newImage, String name, String description, String schedule) {
        this.newImage = newImage;
        this.name = name;
        this.description = description;
        this.schedule = schedule;
    }

    public String getSchedule() {
        return schedule;
    }

    public ChangeStorefrontImageRequestDto setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getNewImage() {
        return newImage;
    }

    public ChangeStorefrontImageRequestDto setNewImage(String newImage) {
        this.newImage = newImage;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChangeStorefrontImageRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ChangeStorefrontImageRequestDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
