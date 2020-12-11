package com.findthebusiness.backend.dto.users;

import com.findthebusiness.backend.entity.Subcategories;

import javax.servlet.http.Cookie;
import java.util.List;

public class ProfileInfoDtoWithAccessToken {
    private ProfileInfoDto profileInfoDto;
    private Cookie accessToken;

    public ProfileInfoDtoWithAccessToken() {
    }

    public ProfileInfoDtoWithAccessToken(ProfileInfoDto profileInfoDto, Cookie accessToken) {
        this.profileInfoDto = profileInfoDto;
        this.accessToken = accessToken;
    }

    public ProfileInfoDto getProfileInfoDto() {
        return profileInfoDto;
    }

    public ProfileInfoDtoWithAccessToken setProfileInfoDto(ProfileInfoDto profileInfoDto) {
        this.profileInfoDto = profileInfoDto;
        return this;
    }

    public Cookie getAccessToken() {
        return accessToken;
    }

    public ProfileInfoDtoWithAccessToken setAccessToken(Cookie accessToken) {
        this.accessToken = accessToken;
        return this;
    }

}
