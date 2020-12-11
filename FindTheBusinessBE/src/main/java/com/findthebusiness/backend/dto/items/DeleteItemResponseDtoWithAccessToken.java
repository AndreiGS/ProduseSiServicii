package com.findthebusiness.backend.dto.items;

import javax.servlet.http.Cookie;

public class DeleteItemResponseDtoWithAccessToken {

    private Cookie accessToken;
    private DeleteItemResponseDto deleteItemResponseDto;

    public DeleteItemResponseDtoWithAccessToken() {
    }

    public DeleteItemResponseDtoWithAccessToken(Cookie accessToken, DeleteItemResponseDto deleteItemResponseDto) {
        this.accessToken = accessToken;
        this.deleteItemResponseDto = deleteItemResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public DeleteItemResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public DeleteItemResponseDto getDeleteItemResponseDto() {
        return deleteItemResponseDto;
    }

    public DeleteItemResponseDtoWithAccessToken setDeleteItemResponseDto(DeleteItemResponseDto deleteItemResponseDto) {
        this.deleteItemResponseDto = deleteItemResponseDto;
        return this;
    }
}
