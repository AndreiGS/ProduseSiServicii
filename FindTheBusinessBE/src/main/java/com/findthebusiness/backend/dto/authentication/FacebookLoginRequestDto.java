package com.findthebusiness.backend.dto.authentication;

public class FacebookLoginRequestDto {
    private String access_token;

    public FacebookLoginRequestDto() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public FacebookLoginRequestDto setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }
}
