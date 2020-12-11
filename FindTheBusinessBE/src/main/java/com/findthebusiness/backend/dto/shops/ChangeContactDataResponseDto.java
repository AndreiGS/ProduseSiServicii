package com.findthebusiness.backend.dto.shops;

public class ChangeContactDataResponseDto {

    private String refreshToken;
    private String csrfToken;

    public ChangeContactDataResponseDto() {
    }

    public ChangeContactDataResponseDto(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChangeContactDataResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChangeContactDataResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
