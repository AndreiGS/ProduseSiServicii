package com.findthebusiness.backend.dto.users;

public class CheckIfCanAddItemResponseDto {

    private String refreshToken;
    private String csrfToken;

    public CheckIfCanAddItemResponseDto() {
    }

    public CheckIfCanAddItemResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public CheckIfCanAddItemResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public CheckIfCanAddItemResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
