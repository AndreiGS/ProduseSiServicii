package com.findthebusiness.backend.dto.users;

public class PasswordChangingRequestDto {

    private String password;

    public PasswordChangingRequestDto() {
    }

    public PasswordChangingRequestDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public PasswordChangingRequestDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
