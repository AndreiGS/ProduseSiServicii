package com.findthebusiness.backend.dto.shops;

public class ChangeStorefrontImageRequestDto {

    private String newImage;
    private String newSmallImage;
    private String name;
    private String description;
    private String schedule;
    private Boolean hasDeletedLargeImage;

    public ChangeStorefrontImageRequestDto() {
    }

    public ChangeStorefrontImageRequestDto(String newImage, String newSmallImage, String name, String description, String schedule, Boolean hasDeletedLargeImage) {
        this.newImage = newImage;
        this.newSmallImage = newSmallImage;
        this.name = name;
        this.description = description;
        this.schedule = schedule;
        this.hasDeletedLargeImage = hasDeletedLargeImage;
    }

    public String getNewSmallImage() {
        return newSmallImage;
    }

    public ChangeStorefrontImageRequestDto setNewSmallImage(String newSmallImage) {
        this.newSmallImage = newSmallImage;
        return this;
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

    public Boolean getHasDeletedLargeImage() {
        return hasDeletedLargeImage;
    }

    public ChangeStorefrontImageRequestDto setHasDeletedLargeImage(Boolean hasDeletedLargeImage) {
        this.hasDeletedLargeImage = hasDeletedLargeImage;
        return this;
    }
}
