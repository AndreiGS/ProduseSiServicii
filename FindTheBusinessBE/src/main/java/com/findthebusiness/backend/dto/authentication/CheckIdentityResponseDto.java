package com.findthebusiness.backend.dto.authentication;

public class CheckIdentityResponseDto {

    private String refreshToken;
    private String csrfToken;

    public CheckIdentityResponseDto() {
    }

    public CheckIdentityResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public CheckIdentityResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public CheckIdentityResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
