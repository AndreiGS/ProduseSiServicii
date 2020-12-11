package com.findthebusiness.backend.dto.items;

public class DeleteItemResponseDto {

    private String refreshToken;
    private String csrfToken;

    public DeleteItemResponseDto() {
    }

    public DeleteItemResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public DeleteItemResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public DeleteItemResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
