package com.findthebusiness.backend.dto.shops;

public class PromoteShopResponse {

    private String refreshToken;
    private String csrfToken;

    public PromoteShopResponse() {
    }

    public PromoteShopResponse(String refreshToken, String csrfToken) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public PromoteShopResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public PromoteShopResponse setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
