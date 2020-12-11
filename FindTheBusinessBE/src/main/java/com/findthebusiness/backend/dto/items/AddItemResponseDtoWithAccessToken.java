package com.findthebusiness.backend.dto.items;

import javax.servlet.http.Cookie;

public class AddItemResponseDtoWithAccessToken {

    private Cookie accessToken;
    private AddItemResponseDto addItemResponseDto;

    public AddItemResponseDtoWithAccessToken() {
    }

    public AddItemResponseDtoWithAccessToken(Cookie accessToken, AddItemResponseDto addItemResponseDto) {
        this.accessToken = accessToken;
        this.addItemResponseDto = addItemResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public AddItemResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AddItemResponseDto getAddItemResponseDto() {
        return addItemResponseDto;
    }

    public AddItemResponseDtoWithAccessToken setAddItemResponseDto(AddItemResponseDto addItemResponseDto) {
        this.addItemResponseDto = addItemResponseDto;
        return this;
    }
}
