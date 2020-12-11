package com.findthebusiness.backend.dto.items;

import com.findthebusiness.backend.entity.Items;

import java.util.List;

public class ItemsByTabResponseDto {

    List<GetItemsResponseDto> items;

    public ItemsByTabResponseDto() {
    }

    public ItemsByTabResponseDto(List<GetItemsResponseDto> items) {
        this.items = items;
    }

    public List<GetItemsResponseDto> getItems() {
        return items;
    }

    public ItemsByTabResponseDto setItems(List<GetItemsResponseDto> items) {
        this.items = items;
        return this;
    }
}
