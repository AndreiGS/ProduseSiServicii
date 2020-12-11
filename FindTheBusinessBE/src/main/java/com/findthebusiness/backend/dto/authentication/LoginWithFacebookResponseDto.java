package com.findthebusiness.backend.dto.authentication;

public class LoginWithFacebookResponseDto {

    String name;

    public LoginWithFacebookResponseDto() {
    }

    public LoginWithFacebookResponseDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LoginWithFacebookResponseDto setName(String name) {
        this.name = name;
        return this;
    }
}
