package com.findthebusiness.backend.dto.authentication;

public class FacebookProfileInfoDto {
    private String name;
    private String id;

    public FacebookProfileInfoDto() {
    }

    public FacebookProfileInfoDto(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public FacebookProfileInfoDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public FacebookProfileInfoDto setId(String id) {
        this.id = id;
        return this;
    }
}
