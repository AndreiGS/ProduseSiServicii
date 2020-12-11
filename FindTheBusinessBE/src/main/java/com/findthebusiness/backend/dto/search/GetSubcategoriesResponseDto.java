package com.findthebusiness.backend.dto.search;

public class GetSubcategoriesResponseDto {

    private String id;
    private String name;
    private String categoryId;

    public GetSubcategoriesResponseDto() {
    }

    public GetSubcategoriesResponseDto(String id, String name, String categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public GetSubcategoriesResponseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetSubcategoriesResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public GetSubcategoriesResponseDto setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }
}
