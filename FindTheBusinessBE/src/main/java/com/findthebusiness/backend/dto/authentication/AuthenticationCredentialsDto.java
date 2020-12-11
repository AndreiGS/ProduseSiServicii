package com.findthebusiness.backend.dto.authentication;

import javax.servlet.http.Cookie;

public class AuthenticationCredentialsDto {

    private Cookie accessToken;
    private String refreshToken;
    private String csrfToken;

    public AuthenticationCredentialsDto() {
    }

    public AuthenticationCredentialsDto(Cookie accessToken, String refreshToken, String csrfToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public AuthenticationCredentialsDto setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthenticationCredentialsDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public AuthenticationCredentialsDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
