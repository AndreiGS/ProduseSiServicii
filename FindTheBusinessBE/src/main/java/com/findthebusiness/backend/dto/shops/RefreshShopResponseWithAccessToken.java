package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class RefreshShopResponseWithAccessToken {

    private RefreshShopResponse refreshShopResponse;
    private Cookie accessToken;

    public RefreshShopResponseWithAccessToken() {
    }

    public RefreshShopResponseWithAccessToken(Cookie accessToken, RefreshShopResponse refreshShopResponse) {
        this.refreshShopResponse = refreshShopResponse;
        this.accessToken = accessToken;
    }

    public RefreshShopResponse getRefreshShopResponse() {
        return refreshShopResponse;
    }

    public RefreshShopResponseWithAccessToken setRefreshShopResponse(RefreshShopResponse refreshShopResponse) {
        this.refreshShopResponse = refreshShopResponse;
        return this;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public RefreshShopResponseWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
