package com.findthebusiness.backend.dto.users;

public class ChargeResponseDto {

    private String csrfToken;
    private String refreshToken;
    private Long amount;

    public ChargeResponseDto() {
    }

    public ChargeResponseDto(String csrfToken, String refreshToken, Long amount) {
        this.csrfToken = csrfToken;
        this.refreshToken = refreshToken;
        this.amount = amount;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ChargeResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ChargeResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public ChargeResponseDto setAmount(Long amount) {
        this.amount = amount;
        return this;
    }
}
