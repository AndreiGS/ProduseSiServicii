package com.findthebusiness.backend.dto.users;

import javax.servlet.http.Cookie;

public class ChargeResponseDtoWithAccessToken {

    private Cookie accessToken;
    private ChargeResponseDto chargeResponseDto;

    public ChargeResponseDtoWithAccessToken() {
    }

    public ChargeResponseDtoWithAccessToken(Cookie accessToken, ChargeResponseDto chargeResponseDto) {
        this.accessToken = accessToken;
        this.chargeResponseDto = chargeResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ChargeResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public ChargeResponseDto getChargeResponseDto() {
        return chargeResponseDto;
    }

    public ChargeResponseDtoWithAccessToken setChargeResponseDto(ChargeResponseDto chargeResponseDto) {
        this.chargeResponseDto = chargeResponseDto;
        return this;
    }
}
