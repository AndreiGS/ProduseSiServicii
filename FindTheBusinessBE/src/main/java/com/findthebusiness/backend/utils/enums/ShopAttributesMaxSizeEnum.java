package com.findthebusiness.backend.utils.enums;

public enum ShopAttributesMaxSizeEnum {
    TITLE_SIZE(40),
    DESCRIPTION_SIZE(255);

    public final java.lang.Integer size;

    ShopAttributesMaxSizeEnum(Integer size) {
        this.size = size;
    }
}
