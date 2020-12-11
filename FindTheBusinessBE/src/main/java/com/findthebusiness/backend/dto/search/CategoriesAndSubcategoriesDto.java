package com.findthebusiness.backend.dto.search;

import java.util.List;

public class CategoriesAndSubcategoriesDto {

    List<GetCategoriesResponseDto> categories;
    List<GetSubcategoriesResponseDto> subcategories;

    public CategoriesAndSubcategoriesDto() {
    }

    public CategoriesAndSubcategoriesDto(List<GetCategoriesResponseDto> categories, List<GetSubcategoriesResponseDto> subcategories) {
        this.categories = categories;
        this.subcategories = subcategories;
    }

    public List<GetCategoriesResponseDto> getCategories() {
        return categories;
    }

    public CategoriesAndSubcategoriesDto setCategories(List<GetCategoriesResponseDto> categories) {
        this.categories = categories;
        return this;
    }

    public List<GetSubcategoriesResponseDto> getSubcategories() {
        return subcategories;
    }

    public CategoriesAndSubcategoriesDto setSubcategories(List<GetSubcategoriesResponseDto> subcategories) {
        this.subcategories = subcategories;
        return this;
    }
}
