package com.findthebusiness.backend.dto.shops;

public class ChangeStorefrontImageResponseDto {

    private String refreshToken;
    private String csrfToken;
    private String newImageURL;
    private String newSmallImageURL;

    public ChangeStorefrontImageResponseDto() {
    }

    public ChangeStorefrontImageResponseDto(String refreshToken, String csrfToken, String newImageURL, String newSmallImageURL) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.newImageURL = newImageURL;
        this.newSmallImageURL = newSmallImageURL;
    }

    public String getNewSmallImageURL() {
        return newSmallImageURL;
    }

    public ChangeStorefrontImageResponseDto setNewSmallImageURL(String newSmallImageURL) {
        this.newSmallImageURL = newSmallImageURL;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChangeStorefrontImageResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChangeStorefrontImageResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getNewImageURL() {
        return newImageURL;
    }

    public ChangeStorefrontImageResponseDto setNewImageURL(String largeImageURL) {
        this.newImageURL = largeImageURL;
        return this;
    }
}
