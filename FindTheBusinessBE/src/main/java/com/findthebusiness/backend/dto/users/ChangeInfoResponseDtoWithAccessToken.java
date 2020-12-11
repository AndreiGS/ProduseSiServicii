package com.findthebusiness.backend.dto.users;

import javax.servlet.http.Cookie;

public class ChangeInfoResponseDtoWithAccessToken {

    private ChangeInfoResponseDto changeInfoResponseDto;
    private Cookie accessToken;

    public ChangeInfoResponseDtoWithAccessToken() {
    }

    public ChangeInfoResponseDtoWithAccessToken(ChangeInfoResponseDto changeInfoResponseDto, Cookie accessToken) {
        this.changeInfoResponseDto = changeInfoResponseDto;
        this.accessToken = accessToken;
    }

    public ChangeInfoResponseDto getChangeInfoResponseDto() {
        return changeInfoResponseDto;
    }

    public ChangeInfoResponseDtoWithAccessToken setChangeInfoResponseDto(ChangeInfoResponseDto changeInfoResponseDto) {
        this.changeInfoResponseDto = changeInfoResponseDto;
        return this;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChangeInfoResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
