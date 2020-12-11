package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.items.AddItemRequestDto;
import com.findthebusiness.backend.dto.items.AddItemResponseDto;
import com.findthebusiness.backend.dto.items.GetItemsResponseDto;
import com.findthebusiness.backend.dto.items.ItemResponseDto;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;

import java.util.List;

public interface ItemsMapper {

    List<GetItemsResponseDto> convertItemsToGetItemsResponseDto(List<Items> items);
    ItemResponseDto convertItemsToItemResponseDto(Items items);
    Items convertAddItemRequestDtoToItems(AddItemRequestDto items, Shops shop, Tabs tabs);

}
