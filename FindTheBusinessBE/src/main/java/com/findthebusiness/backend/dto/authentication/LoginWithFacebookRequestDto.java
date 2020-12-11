package com.findthebusiness.backend.dto.authentication;

public class LoginWithFacebookRequestDto {
    private String fbcode;
    private String url;

    public LoginWithFacebookRequestDto() {
    }

    public LoginWithFacebookRequestDto(String fbcode, String url) {
        this.fbcode = fbcode;
        this.url = url;
    }

    public String getFbcode() {
        return fbcode;
    }

    public LoginWithFacebookRequestDto setFbcode(String fbcode) {
        this.fbcode = fbcode;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public LoginWithFacebookRequestDto setUrl(String url) {
        this.url = url;
        return this;
    }
}
