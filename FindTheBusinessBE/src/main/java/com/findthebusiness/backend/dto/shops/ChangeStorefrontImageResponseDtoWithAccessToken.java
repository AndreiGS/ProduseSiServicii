package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class ChangeStorefrontImageResponseDtoWithAccessToken {

    private Cookie accessToken;
    private ChangeStorefrontImageResponseDto changeStorefrontImageResponseDto;

    public ChangeStorefrontImageResponseDtoWithAccessToken() {
    }

    public ChangeStorefrontImageResponseDtoWithAccessToken(Cookie accessToken, ChangeStorefrontImageResponseDto changeStorefrontImageResponseDto) {
        this.accessToken = accessToken;
        this.changeStorefrontImageResponseDto = changeStorefrontImageResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChangeStorefrontImageResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public ChangeStorefrontImageResponseDto getChangeStorefrontImageResponseDto() {
        return changeStorefrontImageResponseDto;
    }

    public ChangeStorefrontImageResponseDtoWithAccessToken setChangeStorefrontImageResponseDto(ChangeStorefrontImageResponseDto changeStorefrontImageResponseDto) {
        this.changeStorefrontImageResponseDto = changeStorefrontImageResponseDto;
        return this;
    }
}
