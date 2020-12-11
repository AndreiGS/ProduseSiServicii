package com.findthebusiness.backend.dto.users;

public class ChargeRequestDto {

    private String tkn;
    private Long amount;
    private String currency = "ron";

    public ChargeRequestDto() {
    }

    public ChargeRequestDto(String tkn, Long amount, String currency) {
        this.tkn = tkn;
        this.amount = amount;
        this.currency = currency;
    }

    public String getTkn() {
        return tkn;
    }

    public ChargeRequestDto setTkn(String tkn) {
        this.tkn = tkn;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public ChargeRequestDto setAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public ChargeRequestDto setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}
