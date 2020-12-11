package com.findthebusiness.backend.dto.tabs;

import javax.servlet.http.Cookie;

public class DeleteTabResponseDtoWithAccessToken {

    private Cookie accessToken;
    private DeleteTabResponseDto deleteTabResponseDto;

    public DeleteTabResponseDtoWithAccessToken() {
    }

    public DeleteTabResponseDtoWithAccessToken(Cookie accessToken, DeleteTabResponseDto deleteTabResponseDto) {
        this.accessToken = accessToken;
        this.deleteTabResponseDto = deleteTabResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public DeleteTabResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public DeleteTabResponseDto getDeleteTabResponseDto() {
        return deleteTabResponseDto;
    }

    public DeleteTabResponseDtoWithAccessToken setDeleteTabResponseDto(DeleteTabResponseDto deleteTabResponseDto) {
        this.deleteTabResponseDto = deleteTabResponseDto;
        return this;
    }
}
