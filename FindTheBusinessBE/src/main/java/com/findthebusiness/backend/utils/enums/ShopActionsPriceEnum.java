package com.findthebusiness.backend.utils.enums;

public enum ShopActionsPriceEnum {

    REFRESH_SHOP_PRICE(20L),
    PROMOTE_SHOP_PROMOTE_HOME(60L),
    PROMOTE_SHOP_PROMOTE_SEARCHES(60L);

    public final long price;

    ShopActionsPriceEnum(long price) {
        this.price = price;
    }
}
