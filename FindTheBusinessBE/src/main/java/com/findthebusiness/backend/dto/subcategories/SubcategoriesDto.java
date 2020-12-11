package com.findthebusiness.backend.dto.subcategories;

public class SubcategoriesDto {
    private String name;

    public SubcategoriesDto() {
    }

    public SubcategoriesDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SubcategoriesDto setName(String name) {
        this.name = name;
        return this;
    }
}
