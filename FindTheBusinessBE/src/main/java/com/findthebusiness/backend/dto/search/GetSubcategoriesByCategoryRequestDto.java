package com.findthebusiness.backend.dto.search;

import java.util.List;

public class GetSubcategoriesByCategoryRequestDto {

    private GetSubcategoriesResponseDto subcategories;

    public GetSubcategoriesByCategoryRequestDto() {
    }

    public GetSubcategoriesByCategoryRequestDto(GetSubcategoriesResponseDto subcategories) {
        this.subcategories = subcategories;
    }

    public GetSubcategoriesResponseDto getSubcategories() {
        return subcategories;
    }

    public GetSubcategoriesByCategoryRequestDto setSubcategories(GetSubcategoriesResponseDto subcategories) {
        this.subcategories = subcategories;
        return this;
    }
}
