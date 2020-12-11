package com.findthebusiness.backend.dto.tabs;

public class PostTabResponseDto {

    private TabResponseDto tabResponseDto;
    private String refreshToken;
    private String csrfToken;

    public PostTabResponseDto() {
    }

    public PostTabResponseDto(TabResponseDto tabResponseDto, String refreshToken, String csrfToken) {
        this.tabResponseDto = tabResponseDto;
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public TabResponseDto getTabResponseDto() {
        return tabResponseDto;
    }

    public PostTabResponseDto setTabResponseDto(TabResponseDto tabResponseDto) {
        this.tabResponseDto = tabResponseDto;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public PostTabResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public PostTabResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
