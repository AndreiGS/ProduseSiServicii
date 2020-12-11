package com.findthebusiness.backend.dto.search;

public class GetCategoriesResponseDto {

    private String id;
    private String name;

    public GetCategoriesResponseDto() {
    }

    public GetCategoriesResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public GetCategoriesResponseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetCategoriesResponseDto setName(String name) {
        this.name = name;
        return this;
    }
}
