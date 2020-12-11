package com.findthebusiness.backend.dto.shops;

public class ChangeHasAutomaticTokenRefreshResponseDto {

    private String refreshToken;
    private String csrfToken;

    public ChangeHasAutomaticTokenRefreshResponseDto() {
    }

    public ChangeHasAutomaticTokenRefreshResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChangeHasAutomaticTokenRefreshResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChangeHasAutomaticTokenRefreshResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
