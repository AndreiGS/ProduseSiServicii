package com.findthebusiness.backend.dto.shops;

public class ChangePublishedResponseDto {

    private String refreshToken;
    private String csrfToken;
    private boolean isPublished;

    public ChangePublishedResponseDto() {
    }

    public ChangePublishedResponseDto(String refreshToken, String csrfToken, boolean isPublished) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.isPublished = isPublished;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChangePublishedResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChangePublishedResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public ChangePublishedResponseDto setPublished(boolean published) {
        isPublished = published;
        return this;
    }
}
