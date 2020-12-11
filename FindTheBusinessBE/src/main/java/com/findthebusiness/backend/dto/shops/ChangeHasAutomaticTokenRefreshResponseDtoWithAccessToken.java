package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken {

    private Cookie accessToken;
    private ChangeHasAutomaticTokenRefreshResponseDto changeHasAutomaticTokenRefreshResponseDto;

    public ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken() {
    }

    public ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken(Cookie accessToken, ChangeHasAutomaticTokenRefreshResponseDto changeHasAutomaticTokenRefreshResponseDto) {
        this.accessToken = accessToken;
        this.changeHasAutomaticTokenRefreshResponseDto = changeHasAutomaticTokenRefreshResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public ChangeHasAutomaticTokenRefreshResponseDto getChangeHasAutomaticTokenRefreshResponseDto() {
        return changeHasAutomaticTokenRefreshResponseDto;
    }

    public ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken setChangeHasAutomaticTokenRefreshResponseDto(ChangeHasAutomaticTokenRefreshResponseDto changeHasAutomaticTokenRefreshResponseDto) {
        this.changeHasAutomaticTokenRefreshResponseDto = changeHasAutomaticTokenRefreshResponseDto;
        return this;
    }
}
