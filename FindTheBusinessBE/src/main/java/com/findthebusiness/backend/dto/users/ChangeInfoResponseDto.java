package com.findthebusiness.backend.dto.users;

public class ChangeInfoResponseDto {

    private String csrfToken;
    private String refreshToken;

    public ChangeInfoResponseDto() {
    }

    public ChangeInfoResponseDto(String csrfToken, String refreshToken) {
        this.csrfToken = csrfToken;
        this.refreshToken = refreshToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChangeInfoResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChangeInfoResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
