package com.findthebusiness.backend.dto.users;

import javax.servlet.http.Cookie;

public class CheckIfCanAddItemResponseDtoWithAccessToken {

    private Cookie accessToken;
    private CheckIfCanAddItemResponseDto checkIfCanAddItemResponseDto;

    public CheckIfCanAddItemResponseDtoWithAccessToken() {
    }

    public CheckIfCanAddItemResponseDtoWithAccessToken(Cookie accessToken, CheckIfCanAddItemResponseDto checkIfCanAddItemResponseDto) {
        this.accessToken = accessToken;
        this.checkIfCanAddItemResponseDto = checkIfCanAddItemResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public CheckIfCanAddItemResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public CheckIfCanAddItemResponseDto getCheckIfCanAddItemResponseDto() {
        return checkIfCanAddItemResponseDto;
    }

    public CheckIfCanAddItemResponseDtoWithAccessToken setCheckIfCanAddItemResponseDto(CheckIfCanAddItemResponseDto checkIfCanAddItemResponseDto) {
        this.checkIfCanAddItemResponseDto = checkIfCanAddItemResponseDto;
        return this;
    }
}
