package com.findthebusiness.backend.dto.users;

import com.findthebusiness.backend.dto.shops.ShopCardDto;
import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;
import com.findthebusiness.backend.entity.Subcategories;

import java.util.*;

public class ProfileInfoDto {

    private String email;
    private String name;
    private Long balance;
    private List<ShopCardDto> shops;
    private List<SubcategoriesDto> subcategories;
    private Integer smallTokens;
    private Integer mediumTokens;
    private Integer largeTokens;
    private Integer unlimitedTokens;

    private String refreshToken;
    private String csrfToken;

    public ProfileInfoDto() {
    }

    public ProfileInfoDto(String email, String name, Long balance, List<ShopCardDto> shops, List<SubcategoriesDto> subcategories, Integer smallTokens, Integer mediumTokens, Integer largeTokens, Integer unlimitedTokens, String refreshToken, String csrfToken) {
        this.email = email;
        this.name = name;
        this.balance = balance;
        this.shops = shops;
        this.subcategories = subcategories;
        this.smallTokens = smallTokens;
        this.mediumTokens = mediumTokens;
        this.largeTokens = largeTokens;
        this.unlimitedTokens = unlimitedTokens;
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public String getEmail() {
        return email;
    }

    public ProfileInfoDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProfileInfoDto setName(String name) {
        this.name = name;
        return this;
    }

    public Long getBalance() {
        return balance;
    }

    public ProfileInfoDto setBalance(Long balance) {
        this.balance = balance;
        return this;
    }

    public List<ShopCardDto> getShops() {
        return shops;
    }

    public ProfileInfoDto setShops(List<ShopCardDto> shops) {
        this.shops = shops;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ProfileInfoDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public ProfileInfoDto setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public List<SubcategoriesDto> getSubcategories() {
        return subcategories;
    }

    public ProfileInfoDto setSubcategories(List<SubcategoriesDto> subcategories) {
        this.subcategories = subcategories;
        return this;
    }

    public Integer getSmallTokens() {
        return smallTokens;
    }

    public ProfileInfoDto setSmallTokens(Integer smallTokens) {
        this.smallTokens = smallTokens;
        return this;
    }

    public Integer getMediumTokens() {
        return mediumTokens;
    }

    public ProfileInfoDto setMediumTokens(Integer mediumTokens) {
        this.mediumTokens = mediumTokens;
        return this;
    }

    public Integer getLargeTokens() {
        return largeTokens;
    }

    public ProfileInfoDto setLargeTokens(Integer largeTokens) {
        this.largeTokens = largeTokens;
        return this;
    }

    public Integer getUnlimitedTokens() {
        return unlimitedTokens;
    }

    public ProfileInfoDto setUnlimitedTokens(Integer unlimited) {
        this.unlimitedTokens = unlimited;
        return this;
    }
}
