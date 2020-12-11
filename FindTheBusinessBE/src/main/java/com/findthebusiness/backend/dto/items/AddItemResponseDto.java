package com.findthebusiness.backend.dto.items;

public class AddItemResponseDto {

    private String refreshToken;
    private String csrfToken;
    private ItemResponseDto itemResponseDto;

    public AddItemResponseDto() {
    }

    public AddItemResponseDto(String refreshToken, String csrfToken, ItemResponseDto itemResponseDto) {
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
        this.itemResponseDto = itemResponseDto;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AddItemResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public AddItemResponseDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public ItemResponseDto getItemResponseDto() {
        return itemResponseDto;
    }

    public AddItemResponseDto setItemResponseDto(ItemResponseDto itemResponseDto) {
        this.itemResponseDto = itemResponseDto;
        return this;
    }
}
