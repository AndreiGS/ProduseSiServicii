package com.findthebusiness.backend.dto.users;

import javax.servlet.http.Cookie;

public class AddShopTokenResponseDtoWithAccessToken {

    private Cookie accessToken;
    private AddShopTokenResponseDto addShopTokenResponseDto;

    public AddShopTokenResponseDtoWithAccessToken() {
    }

    public AddShopTokenResponseDtoWithAccessToken(Cookie accessToken, AddShopTokenResponseDto addShopTokenResponseDto) {
        this.accessToken = accessToken;
        this.addShopTokenResponseDto = addShopTokenResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public AddShopTokenResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AddShopTokenResponseDto getAddShopTokenResponseDto() {
        return addShopTokenResponseDto;
    }

    public AddShopTokenResponseDtoWithAccessToken setAddShopTokenResponseDto(AddShopTokenResponseDto addShopTokenResponseDto) {
        this.addShopTokenResponseDto = addShopTokenResponseDto;
        return this;
    }
}
