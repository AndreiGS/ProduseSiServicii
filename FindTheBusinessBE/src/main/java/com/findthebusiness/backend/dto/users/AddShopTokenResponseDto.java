package com.findthebusiness.backend.dto.users;

public class AddShopTokenResponseDto {

    private Long balanceSubtracted;

    private String refreshToken;
    private String csrfToken;

    public AddShopTokenResponseDto() {
    }

    public AddShopTokenResponseDto(Long balanceSubtracted, String refreshToken, String csrfToken) {
        this.balanceSubtracted = balanceSubtracted;
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public Long getBalanceSubtracted() {
        return balanceSubtracted;
    }

    public AddShopTokenResponseDto setBalanceSubtracted(Long balanceSubtracted) {
        this.balanceSubtracted = balanceSubtracted;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AddShopTokenResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public AddShopTokenResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }


}
