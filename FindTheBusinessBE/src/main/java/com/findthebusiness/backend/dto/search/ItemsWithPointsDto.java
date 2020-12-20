package com.findthebusiness.backend.dto.search;

import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.utils.enums.ShopPromotionTypesEnum;

import java.util.Date;

public class ItemsWithPointsDto implements Comparable{

    private int point;
    private Items items;

    public ItemsWithPointsDto() {
    }

    public ItemsWithPointsDto(int point, Items items) {
        this.point = point;
        this.items = items;
    }

    public int getPoint() {
        return point;
    }

    public ItemsWithPointsDto setPoint(int point) {
        this.point = point;
        return this;
    }

    public Items getItems() {
        return items;
    }

    public ItemsWithPointsDto setItems(Items items) {
        this.items = items;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        ItemsWithPointsDto item = (ItemsWithPointsDto) o;

        if(this.getPoint() < item.getPoint()) {
            return -1;
        }
        else if(this.getPoint() > item.getPoint()) {
            return 1;
        }
        else {
            boolean isNewItemPromoted = this.getItems().getShop().getPromotedInSearches();
            boolean isMidPromoted = item.getItems().getShop().getPromotedInSearches();
            if(isNewItemPromoted == true && isMidPromoted == false) {
                return -1;
            } else if(isNewItemPromoted == false && isMidPromoted == true) {
                return 1;
            } else {
                Date newItemRefreshDate = this.getItems().getShop().getRefreshedAt();
                Date isMidRefreshDate = item.getItems().getShop().getRefreshedAt();

                if(newItemRefreshDate.compareTo(isMidRefreshDate) > 0) {
                    return -1;
                } else if(newItemRefreshDate.compareTo(isMidRefreshDate) < 0) {
                    return 1;
                } else {
                    Date newItemBoughtAt = this.getItems().getShop().getBoughtAt();
                    Date isMidBoughtAt = item.getItems().getShop().getBoughtAt();

                    if(newItemBoughtAt.compareTo(isMidBoughtAt) > 0) {
                        return -1;
                    } else if(newItemBoughtAt.compareTo(isMidBoughtAt) < 0) {
                        return 1;
                    }
                }
            }
        }
        if(item.getItems() == this.getItems())
            return 0;
        else {
            int thisItemTitleLength = this.getItems().getTitle().length();
            int otherItemTitleLength = item.getItems().getTitle().length();

            if(thisItemTitleLength < otherItemTitleLength)
                return -1;
            return 1;
        }
    }
}
