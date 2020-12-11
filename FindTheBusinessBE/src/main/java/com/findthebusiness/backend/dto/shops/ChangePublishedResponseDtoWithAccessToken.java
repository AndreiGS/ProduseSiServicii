package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class ChangePublishedResponseDtoWithAccessToken {

    private Cookie accessToken;
    private ChangePublishedResponseDto changePublishedResponseDto;

    public ChangePublishedResponseDtoWithAccessToken() {
    }

    public ChangePublishedResponseDtoWithAccessToken(Cookie accessToken, ChangePublishedResponseDto changePublishedResponseDto) {
        this.accessToken = accessToken;
        this.changePublishedResponseDto = changePublishedResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChangePublishedResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public ChangePublishedResponseDto getChangePublishedResponseDto() {
        return changePublishedResponseDto;
    }

    public ChangePublishedResponseDtoWithAccessToken setChangePublishedResponseDto(ChangePublishedResponseDto changePublishedResponseDto) {
        this.changePublishedResponseDto = changePublishedResponseDto;
        return this;
    }
}
