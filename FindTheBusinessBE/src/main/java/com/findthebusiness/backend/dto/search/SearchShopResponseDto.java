package com.findthebusiness.backend.dto.search;

import java.util.List;

public class SearchShopResponseDto {

    private ShopDto shop;
    private List<SearchItemsResponseDto> itemsIds;

    public SearchShopResponseDto() {
    }

    public SearchShopResponseDto(ShopDto shop, List<SearchItemsResponseDto> itemsIds) {
        this.shop = shop;
        this.itemsIds = itemsIds;
    }

    public ShopDto getShop() {
        return shop;
    }

    public SearchShopResponseDto setShop(ShopDto shop) {
        this.shop = shop;
        return this;
    }

    public List<SearchItemsResponseDto> getItemsIds() {
        return itemsIds;
    }

    public SearchShopResponseDto setItemsIds(List<SearchItemsResponseDto> itemsIds) {
        this.itemsIds = itemsIds;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return this.shop.getId().equals(((SearchShopResponseDto) obj).getShop().getId());
    }
}
