package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class DeleteShopResponseDtoWithAccessToken {

    private Cookie accessToken;
    private DeleteShopResponseDto deleteShopResponseDto;

    public DeleteShopResponseDtoWithAccessToken() {
    }

    public DeleteShopResponseDtoWithAccessToken(Cookie accessToken, DeleteShopResponseDto deleteShopResponseDto) {
        this.accessToken = accessToken;
        this.deleteShopResponseDto = deleteShopResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public DeleteShopResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public DeleteShopResponseDto getDeleteShopResponseDto() {
        return deleteShopResponseDto;
    }

    public DeleteShopResponseDtoWithAccessToken setDeleteShopResponseDto(DeleteShopResponseDto deleteShopResponseDto) {
        this.deleteShopResponseDto = deleteShopResponseDto;
        return this;
    }
}
