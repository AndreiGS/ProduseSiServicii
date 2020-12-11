package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.items.AddItemRequestDto;
import com.findthebusiness.backend.dto.items.GetItemsResponseDto;
import com.findthebusiness.backend.dto.items.ItemResponseDto;
import com.findthebusiness.backend.dto.tabs.TabWithoutIdDto;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;
import com.findthebusiness.backend.mapper.mapper_repository.ItemsMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemsMapperImpl implements ItemsMapper {

    private final ModelMapper modelMapper;
    private final TabMapperImpl tabMapper;

    public ItemsMapperImpl(ModelMapper modelMapper, TabMapperImpl tabMapper) {
        this.modelMapper = modelMapper;
        this.tabMapper = tabMapper;
    }

    @Override
    public List<GetItemsResponseDto> convertItemsToGetItemsResponseDto(List<Items> items) {
        List<GetItemsResponseDto> itemsDto = new ArrayList<>();
        for(Items item : items) {
            GetItemsResponseDto getItemsResponseDto = modelMapper.map(item, GetItemsResponseDto.class);

            Tabs itemTab = item.getTabs();
            getItemsResponseDto.setTabName((itemTab == null) ? null : itemTab.getName());
            itemsDto.add(getItemsResponseDto);
        }
        return itemsDto;
    }

    @Override
    public ItemResponseDto convertItemsToItemResponseDto(Items items) {
        ItemResponseDto itemResponseDto = modelMapper.map(items, ItemResponseDto.class);

        Tabs itemTab = items.getTabs();
        TabWithoutIdDto tabWithoutIdDto = (itemTab == null) ? null : tabMapper.convertTabToTabWithoutIdDto(itemTab);
        itemResponseDto.setTabWithoutIdDto(tabWithoutIdDto);

        return itemResponseDto;
    }

    @Override
    public Items convertAddItemRequestDtoToItems(AddItemRequestDto items, Shops shop, Tabs tabs) {
        Items item = modelMapper.map(items, Items.class);
        item.setShop(shop);
        item.setTabs(tabs);

        return item;
    }
}
