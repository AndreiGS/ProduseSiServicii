package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.tabs.PostTabRequestDto;
import com.findthebusiness.backend.dto.tabs.TabWithoutIdDto;
import com.findthebusiness.backend.entity.Tabs;

public interface TabMapper {

    Tabs convertPostTabRequestDtoToTabs(PostTabRequestDto postTabRequestDto);
    TabWithoutIdDto convertTabToTabWithoutIdDto(Tabs tabs);

}
