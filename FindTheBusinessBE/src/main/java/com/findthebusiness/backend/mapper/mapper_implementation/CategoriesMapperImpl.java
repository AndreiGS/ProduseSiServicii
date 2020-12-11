package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.search.GetCategoriesResponseDto;
import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.mapper.mapper_repository.CategoriesMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriesMapperImpl implements CategoriesMapper {

    private final ModelMapper modelMapper;

    public CategoriesMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GetCategoriesResponseDto> convertCategoriesToGetCategoriesResponseDto(List<Categories> categories) {
        List<GetCategoriesResponseDto> categoriesDto = new ArrayList<>();
        for(Categories category : categories) {
            categoriesDto.add(modelMapper.map(category, GetCategoriesResponseDto.class));
        }

        return categoriesDto;
    }
}
