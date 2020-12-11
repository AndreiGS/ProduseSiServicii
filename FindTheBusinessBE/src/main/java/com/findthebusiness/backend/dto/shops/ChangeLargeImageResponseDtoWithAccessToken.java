package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class ChangeLargeImageResponseDtoWithAccessToken {

    private Cookie accessToken;
    private ChangeLargeImageResponseDto changeLargeImageResponseDto;

    public ChangeLargeImageResponseDtoWithAccessToken() {
    }

    public ChangeLargeImageResponseDtoWithAccessToken(Cookie accessToken, ChangeLargeImageResponseDto changeLargeImageResponseDto) {
        this.accessToken = accessToken;
        this.changeLargeImageResponseDto = changeLargeImageResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChangeLargeImageResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public ChangeLargeImageResponseDto getChangeLargeImageResponseDto() {
        return changeLargeImageResponseDto;
    }

    public ChangeLargeImageResponseDtoWithAccessToken setChangeLargeImageResponseDto(ChangeLargeImageResponseDto changeLargeImageResponseDto) {
        this.changeLargeImageResponseDto = changeLargeImageResponseDto;
        return this;
    }
}
