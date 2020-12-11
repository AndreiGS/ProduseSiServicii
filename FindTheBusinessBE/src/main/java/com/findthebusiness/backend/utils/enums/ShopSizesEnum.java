package com.findthebusiness.backend.utils.enums;

public enum ShopSizesEnum {

    FREE(1, 0L),
    SMALL(5, 20L),
    MEDIUM(15, 50L),
    LARGE(40, 120L),
    UNLIMITED(100, 250L);

    public final java.lang.Integer size;
    public final Long price;

    ShopSizesEnum(Integer size, Long price) {
        this.size = size;
        this.price = price;
    }
}
