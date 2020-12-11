package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.search.GetCategoriesResponseDto;
import com.findthebusiness.backend.entity.Categories;

import java.util.List;

public interface CategoriesMapper {

    List<GetCategoriesResponseDto> convertCategoriesToGetCategoriesResponseDto(List<Categories> categories);

}
