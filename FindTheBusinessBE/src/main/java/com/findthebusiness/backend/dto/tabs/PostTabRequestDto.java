package com.findthebusiness.backend.dto.tabs;

public class PostTabRequestDto {

    private String name;

    public PostTabRequestDto() {
    }

    public PostTabRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PostTabRequestDto setName(String name) {
        this.name = name;
        return this;
    }
}
