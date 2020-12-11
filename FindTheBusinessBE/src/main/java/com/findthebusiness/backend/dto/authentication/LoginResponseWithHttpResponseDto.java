package com.findthebusiness.backend.dto.authentication;

import javax.servlet.http.Cookie;

public class LoginResponseWithHttpResponseDto {
    private Cookie accessToken;
    private String refreshToken;
    private String csrfToken;
    private String userRole;

    public LoginResponseWithHttpResponseDto() {
    }

    public LoginResponseWithHttpResponseDto(Cookie accessToken, String refreshToken, String csrfToken, String userRole) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public LoginResponseWithHttpResponseDto setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LoginResponseWithHttpResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public LoginResponseWithHttpResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public LoginResponseWithHttpResponseDto setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }
}
