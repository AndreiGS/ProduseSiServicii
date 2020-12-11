package com.findthebusiness.backend.utils.enums;

public enum ShopActionsPriceEnum {

    REFRESH_SHOP_PRICE(5L),
    PROMOTE_SHOP_PROMOTE(60L);

    public final long price;

    ShopActionsPriceEnum(long price) {
        this.price = price;
    }
}
