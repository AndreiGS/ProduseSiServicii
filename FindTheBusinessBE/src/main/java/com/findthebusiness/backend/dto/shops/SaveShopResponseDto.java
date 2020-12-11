package com.findthebusiness.backend.dto.shops;

public class SaveShopResponseDto {

    private String refreshToken;
    private String csrfToken;
    private String shopId;
    private String smallPhotoUrl;

    public SaveShopResponseDto() {
    }

    public SaveShopResponseDto(String refreshToken, String csrfToken, String shopId, String smallPhotoUrl) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.shopId = shopId;
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public SaveShopResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public SaveShopResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getShopId() {
        return shopId;
    }

    public SaveShopResponseDto setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public SaveShopResponseDto setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
        return this;
    }
}
