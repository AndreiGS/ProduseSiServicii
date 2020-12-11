package com.findthebusiness.backend.dto.users;

import javax.servlet.http.Cookie;

public class PasswordChangingResponseDtoWithAccessToken {

    private PasswordChangingResponseDto passwordChangingResponseDto;
    private Cookie accessToken;

    public PasswordChangingResponseDtoWithAccessToken() {
    }

    public PasswordChangingResponseDtoWithAccessToken(PasswordChangingResponseDto passwordChangingResponseDto, Cookie accessToken) {
        this.passwordChangingResponseDto = passwordChangingResponseDto;
        this.accessToken = accessToken;
    }

    public PasswordChangingResponseDto getPasswordChangingResponseDto() {
        return passwordChangingResponseDto;
    }

    public PasswordChangingResponseDtoWithAccessToken setPasswordChangingResponseDto(PasswordChangingResponseDto passwordChangingResponseDto) {
        this.passwordChangingResponseDto = passwordChangingResponseDto;
        return this;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public PasswordChangingResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
