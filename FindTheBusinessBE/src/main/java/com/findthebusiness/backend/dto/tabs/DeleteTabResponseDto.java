package com.findthebusiness.backend.dto.tabs;

public class DeleteTabResponseDto {

    private String refreshToken;
    private String csrfToken;

    public DeleteTabResponseDto() {
    }

    public DeleteTabResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public DeleteTabResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public DeleteTabResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
