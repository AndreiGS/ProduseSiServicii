package com.findthebusiness.backend.dto.authentication;

public class SendResetPasswordEmailRequestDto {
    private String email;

    public SendResetPasswordEmailRequestDto() {
    }

    public SendResetPasswordEmailRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public SendResetPasswordEmailRequestDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
