package com.findthebusiness.backend.dto.shops;

public class DeleteShopResponseDto {

    private String refreshToken;
    private String csrfToken;

    public DeleteShopResponseDto() {
    }

    public DeleteShopResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public DeleteShopResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public DeleteShopResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
