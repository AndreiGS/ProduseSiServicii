package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class SaveShopResponseDtoWithAccessToken {

    private SaveShopResponseDto saveShopResponseDto;
    private Cookie accessToken;

    public SaveShopResponseDtoWithAccessToken() {
    }

    public SaveShopResponseDtoWithAccessToken(SaveShopResponseDto saveShopResponseDto, Cookie accessToken) {
        this.saveShopResponseDto = saveShopResponseDto;
        this.accessToken = accessToken;
    }

    public SaveShopResponseDto getSaveShopResponseDto() {
        return saveShopResponseDto;
    }

    public SaveShopResponseDtoWithAccessToken setSaveShopResponseDto(SaveShopResponseDto saveShopResponseDto) {
        this.saveShopResponseDto = saveShopResponseDto;
        return this;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public SaveShopResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
