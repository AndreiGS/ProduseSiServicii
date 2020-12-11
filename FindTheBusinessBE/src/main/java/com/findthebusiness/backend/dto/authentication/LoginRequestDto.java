package com.findthebusiness.backend.dto.authentication;

public class LoginRequestDto {

    private String email;
    private String password;

    public LoginRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public LoginRequestDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequestDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
