package com.findthebusiness.backend.dto.search;

public class SearchItemsResponseDto {

    private String id;

    public SearchItemsResponseDto() {
    }

    public SearchItemsResponseDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public SearchItemsResponseDto setId(String id) {
        this.id = id;
        return this;
    }
}
