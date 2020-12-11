package com.findthebusiness.backend.dto.shops;

public class ChangeLargeImageResponseDto {

    private String refreshToken;
    private String csrfToken;
    private String largeImageURL;

    public ChangeLargeImageResponseDto() {
    }

    public ChangeLargeImageResponseDto(String refreshToken, String csrfToken, String largeImageURL) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.largeImageURL = largeImageURL;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChangeLargeImageResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChangeLargeImageResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public ChangeLargeImageResponseDto setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
        return this;
    }
}
