package com.findthebusiness.backend.dto.shops;

public class CheckIfShopOwnerResponseDto {

    private String refreshToken;
    private String csrfToken;

    public CheckIfShopOwnerResponseDto() {
    }

    public CheckIfShopOwnerResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public CheckIfShopOwnerResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public CheckIfShopOwnerResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
