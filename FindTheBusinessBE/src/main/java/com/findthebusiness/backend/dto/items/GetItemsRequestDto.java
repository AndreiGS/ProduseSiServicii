package com.findthebusiness.backend.dto.items;

import java.util.List;

public class GetItemsRequestDto {

    private List<String> foundItemsIds;
    private String tab;

    public GetItemsRequestDto() {
    }

    public GetItemsRequestDto(List<String> foundItemsIds, String tab) {
        this.foundItemsIds = foundItemsIds;
        this.tab = tab;
    }

    public List<String> getFoundItemsIds() {
        return foundItemsIds;
    }

    public GetItemsRequestDto setFoundItemsIds(List<String> foundItemsIds) {
        this.foundItemsIds = foundItemsIds;
        return this;
    }

    public String getTab() {
        return tab;
    }

    public GetItemsRequestDto setTab(String tab) {
        this.tab = tab;
        return this;
    }
}
