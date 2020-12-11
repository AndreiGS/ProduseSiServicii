package com.findthebusiness.backend.dto.users;

public class PasswordChangingResponseDto {

    private String csrfToken;
    private String refreshToken;

    public PasswordChangingResponseDto() {
    }

    public PasswordChangingResponseDto(String csrfToken, String refreshToken) {
        this.csrfToken = csrfToken;
        this.refreshToken = refreshToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public PasswordChangingResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public PasswordChangingResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
