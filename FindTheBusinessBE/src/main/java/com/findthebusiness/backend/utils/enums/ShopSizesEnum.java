package com.findthebusiness.backend.utils.enums;

public enum ShopSizesEnum {

    FREE(1, 0L),
    SMALL(5, 0L),
    MEDIUM(15, 0L),
    LARGE(40, 0L),
    UNLIMITED(100, 0L);

    public final java.lang.Integer size;
    public final Long price;

    ShopSizesEnum(Integer size, Long price) {
        this.size = size;
        this.price = price;
    }
}
