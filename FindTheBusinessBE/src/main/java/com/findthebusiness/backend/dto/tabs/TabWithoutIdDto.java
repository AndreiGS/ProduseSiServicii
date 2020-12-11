package com.findthebusiness.backend.dto.tabs;

public class TabWithoutIdDto {

    private String name;

    public TabWithoutIdDto() {
    }

    public TabWithoutIdDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TabWithoutIdDto setName(String name) {
        this.name = name;
        return this;
    }
}
