package com.findthebusiness.backend.dto.shops;

import java.util.*;

public class PromotedShopsPageDto {
    List<ShopCardDto> shops;
    Integer totalPages;

    public PromotedShopsPageDto() {
    }

    public List<ShopCardDto> getShops() {
        return shops;
    }

    public PromotedShopsPageDto setShops(List<ShopCardDto> shops) {
        this.shops = shops;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public PromotedShopsPageDto setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
