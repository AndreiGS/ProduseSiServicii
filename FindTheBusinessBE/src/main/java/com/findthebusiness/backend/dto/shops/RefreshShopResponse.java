package com.findthebusiness.backend.dto.shops;

public class RefreshShopResponse {

    private String refreshToken;
    private String csrfToken;

    public RefreshShopResponse() {
    }

    public RefreshShopResponse(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public RefreshShopResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public RefreshShopResponse setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
