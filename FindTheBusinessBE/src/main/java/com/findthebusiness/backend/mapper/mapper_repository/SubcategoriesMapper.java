package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.search.GetSubcategoriesByCategoryRequestDto;
import com.findthebusiness.backend.dto.search.GetSubcategoriesResponseDto;
import com.findthebusiness.backend.entity.Subcategories;

import java.util.List;

public interface SubcategoriesMapper {

    List<GetSubcategoriesResponseDto> convertSubcategoriesToGetSubcategoriesResponseDto(List<Subcategories> subcategories);
    List<GetSubcategoriesByCategoryRequestDto> convertSubcategoriesToGetSubcategoriesByCategoryRequestDto(List<Subcategories> subcategories);

}
