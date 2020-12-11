package com.findthebusiness.backend.dto.shops;

import javax.servlet.http.Cookie;

public class CheckIfShopOwnerReponseDtoWithAccessToken {

    private Cookie accessToken;
    private CheckIfShopOwnerResponseDto checkIfShopOwnerResponseDto;

    public CheckIfShopOwnerReponseDtoWithAccessToken() {
    }

    public CheckIfShopOwnerReponseDtoWithAccessToken(Cookie accessToken, CheckIfShopOwnerResponseDto checkIfShopOwnerResponseDto) {
        this.accessToken = accessToken;
        this.checkIfShopOwnerResponseDto = checkIfShopOwnerResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public CheckIfShopOwnerReponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public CheckIfShopOwnerResponseDto getCheckIfShopOwnerResponseDto() {
        return checkIfShopOwnerResponseDto;
    }

    public CheckIfShopOwnerReponseDtoWithAccessToken setCheckIfShopOwnerResponseDto(CheckIfShopOwnerResponseDto checkIfShopOwnerResponseDto) {
        this.checkIfShopOwnerResponseDto = checkIfShopOwnerResponseDto;
        return this;
    }
}
