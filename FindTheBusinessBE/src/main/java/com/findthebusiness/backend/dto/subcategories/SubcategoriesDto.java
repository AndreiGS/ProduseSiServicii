package com.findthebusiness.backend.dto.subcategories;

public class SubcategoriesDto {
    private String name;
    private String categoryId;

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

    public String getCategoryId() {
        return categoryId;
    }

    public SubcategoriesDto setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }
}
