package com.findthebusiness.backend.dto.search;

import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.utils.enums.ShopPromotionTypesEnum;

import java.util.Date;

public class ShopsByTitleWithPointsDto implements Comparable{

    private int points;
    private Shops shops;

    public ShopsByTitleWithPointsDto() {
    }

    public ShopsByTitleWithPointsDto(int points, Shops shops) {
        this.points = points;
        this.shops = shops;
    }

    public int getPoints() {
        return points;
    }

    public ShopsByTitleWithPointsDto setPoints(int points) {
        this.points = points;
        return this;
    }

    public Shops getShops() {
        return shops;
    }

    public ShopsByTitleWithPointsDto setShops(Shops shops) {
        this.shops = shops;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        ShopsByTitleWithPointsDto shop = (ShopsByTitleWithPointsDto) o;

        if(this.getPoints() < shop.getPoints()) {
            return -1;
        }
        else if(this.getPoints() > shop.getPoints()) {
            return 1;
        }
        else {
            boolean isNewItemPromoted = this.getShops().getPromotedInSearches();
            boolean isMidPromoted = shop.getShops().getPromotedInSearches();
            if(isNewItemPromoted == true && isMidPromoted == false) {
                return -1;
            } else if(isNewItemPromoted == false && isMidPromoted == true) {
                return 1;
            } else {
                Date newItemRefreshDate = this.getShops().getRefreshedAt();
                Date isMidRefreshDate = shop.getShops().getRefreshedAt();

                if(newItemRefreshDate.compareTo(isMidRefreshDate) > 0) {
                    return -1;
                } else if(newItemRefreshDate.compareTo(isMidRefreshDate) < 0) {
                    return 1;
                } else {
                    Date newItemBoughtAt = this.getShops().getBoughtAt();
                    Date isMidBoughtAt = shop.getShops().getBoughtAt();

                    if(newItemBoughtAt.compareTo(isMidBoughtAt) > 0) {
                        return -1;
                    } else if(newItemBoughtAt.compareTo(isMidBoughtAt) < 0) {
                        return 1;
                    }
                }
            }
        }
        if(shop == this)
            return 0;
        else {
            int thisItemTitleLength = this.getShops().getName().length();
            int otherItemTitleLength = shop.getShops().getName().length();

            if(thisItemTitleLength < otherItemTitleLength)
                return -1;
            return 1;
        }
    }
}
