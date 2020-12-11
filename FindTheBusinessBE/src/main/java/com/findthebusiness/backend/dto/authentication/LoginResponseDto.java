package com.findthebusiness.backend.dto.authentication;

public class LoginResponseDto {
    private String refreshToken;
    private String csrfToken;
    private String userRole;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String refreshToken, String csrfToken, String userRole) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public LoginResponseDto setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LoginResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public LoginResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
