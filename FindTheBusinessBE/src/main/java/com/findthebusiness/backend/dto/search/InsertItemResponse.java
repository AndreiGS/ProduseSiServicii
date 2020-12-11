package com.findthebusiness.backend.dto.search;

import com.findthebusiness.backend.entity.Items;

public class InsertItemResponse {

    private ItemsWithPointsDto[] bsList;
    private int bsListSize;

    public InsertItemResponse() {
    }

    public InsertItemResponse(ItemsWithPointsDto[] bsList, int bsListSize) {
        this.bsList = bsList;
        this.bsListSize = bsListSize;
    }

    public ItemsWithPointsDto[] getBsList() {
        return bsList;
    }

    public InsertItemResponse setBsList(ItemsWithPointsDto[] bsList) {
        this.bsList = bsList;
        return this;
    }

    public int getBsListSize() {
        return bsListSize;
    }

    public InsertItemResponse setBsListSize(int bsListSize) {
        this.bsListSize = bsListSize;
        return this;
    }
}
