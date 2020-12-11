package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.search.GetCategoriesResponseDto;
import com.findthebusiness.backend.dto.search.GetSubcategoriesByCategoryRequestDto;
import com.findthebusiness.backend.dto.search.GetSubcategoriesResponseDto;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.mapper.mapper_repository.SubcategoriesMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubcategoriesMapperImpl implements SubcategoriesMapper {

    private final ModelMapper modelMapper;

    public SubcategoriesMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GetSubcategoriesResponseDto> convertSubcategoriesToGetSubcategoriesResponseDto(List<Subcategories> subcategories) {
        List<GetSubcategoriesResponseDto> subcategoriesDto = new ArrayList<>();
        for(Subcategories subcategory : subcategories) {

            GetSubcategoriesResponseDto getSubcategoriesResponseDto = modelMapper.map(subcategory, GetSubcategoriesResponseDto.class);
            getSubcategoriesResponseDto.setCategoryId(subcategory.getCategory().getId());
            subcategoriesDto.add(getSubcategoriesResponseDto);

        }

        return subcategoriesDto;
    }

    @Override
    public List<GetSubcategoriesByCategoryRequestDto> convertSubcategoriesToGetSubcategoriesByCategoryRequestDto(List<Subcategories> subcategories) {
        List<GetSubcategoriesByCategoryRequestDto> subcategoriesDto = new ArrayList<>();
        for(Subcategories subcategory : subcategories) {
            subcategoriesDto.add(modelMapper.map(subcategory, GetSubcategoriesByCategoryRequestDto.class));
        }

        return subcategoriesDto;
    }
}
