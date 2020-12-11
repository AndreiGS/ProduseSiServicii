package com.findthebusiness.backend.dto.tabs;

import javax.servlet.http.Cookie;

public class PostTabResponseDtoWithAccessToken {

    private Cookie accessToken;
    private PostTabResponseDto postTabResponseDto;

    public PostTabResponseDtoWithAccessToken() {
    }

    public PostTabResponseDtoWithAccessToken(Cookie accessToken, PostTabResponseDto postTabResponseDto) {
        this.accessToken = accessToken;
        this.postTabResponseDto = postTabResponseDto;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public PostTabResponseDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public PostTabResponseDto getPostTabResponseDto() {
        return postTabResponseDto;
    }

    public PostTabResponseDtoWithAccessToken setPostTabResponseDto(PostTabResponseDto postTabResponseDto) {
        this.postTabResponseDto = postTabResponseDto;
        return this;
    }
}
