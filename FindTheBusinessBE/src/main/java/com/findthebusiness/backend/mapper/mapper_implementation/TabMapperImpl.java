package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.tabs.PostTabRequestDto;
import com.findthebusiness.backend.dto.tabs.TabWithoutIdDto;
import com.findthebusiness.backend.entity.Tabs;
import com.findthebusiness.backend.mapper.mapper_repository.TabMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TabMapperImpl implements TabMapper {
    private final ModelMapper modelMapper;

    public TabMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Tabs convertPostTabRequestDtoToTabs(PostTabRequestDto postTabRequestDto) {
        return modelMapper.map(postTabRequestDto, Tabs.class);
    }

    @Override
    public TabWithoutIdDto convertTabToTabWithoutIdDto(Tabs tabs) {
        return modelMapper.map(tabs, TabWithoutIdDto.class);
    }
}
