package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class PromoteShopResponseWithAccessToken {

    private Cookie accessToken;
    private PromoteShopResponse promoteShopResponse;

    public PromoteShopResponseWithAccessToken() {
    }

    public PromoteShopResponseWithAccessToken(Cookie accessToken, PromoteShopResponse promoteShopResponse) {
        this.accessToken = accessToken;
        this.promoteShopResponse = promoteShopResponse;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public PromoteShopResponseWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public PromoteShopResponse getPromoteShopResponse() {
        return promoteShopResponse;
    }

    public PromoteShopResponseWithAccessToken setPromoteShopResponse(PromoteShopResponse promoteShopResponse) {
        this.promoteShopResponse = promoteShopResponse;
        return this;
    }
}
