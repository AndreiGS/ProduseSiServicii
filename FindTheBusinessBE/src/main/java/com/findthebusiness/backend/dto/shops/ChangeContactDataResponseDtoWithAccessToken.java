package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class ChangeContactDataResponseDtoWithAccessToken {

    private Cookie accessToken;
    private ChangeContactDataResponseDto changeContactDataResponseDto;

    public ChangeContactDataResponseDtoWithAccessToken() {
    }

    public ChangeContactDataResponseDtoWithAccessToken(Cookie accessToken, ChangeContactDataResponseDto changeContactDataResponseDto) {
        this.accessToken = accessToken;
        this.changeContactDataResponseDto = changeContactDataResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChangeContactDataResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public ChangeContactDataResponseDto getChangeContactDataResponseDto() {
        return changeContactDataResponseDto;
    }

    public ChangeContactDataResponseDtoWithAccessToken setChangeContactDataResponseDto(ChangeContactDataResponseDto changeContactDataResponseDto) {
        this.changeContactDataResponseDto = changeContactDataResponseDto;
        return this;
    }
}
