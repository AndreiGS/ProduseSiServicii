package com.findthebusiness.backend.dto.shops;

import com.findthebusiness.backend.dto.tabs.TabResponseDto;

import java.util.Set;

public class StoreFrontRequestDto {

    private String name;
    private String description;
    private String schedule;
    private String email;
    private String smallPhoto;
    private String largePhoto;
    private String phone;
    private String websiteLink;
    private String address;
    private String county;
    private Double rating;
    private Double price;
    private Boolean isPromotedInHome;
    private Boolean isPromotedInSearches;
    private Boolean hasAutomaticTokenRefresh;
    private Set<TabResponseDto> tabs;

    public StoreFrontRequestDto() {
    }

    public StoreFrontRequestDto(String name, String description, String schedule, String email, String smallPhoto, String largePhoto, String phone, String websiteLink, String address, String county, Double rating, Double price, Boolean isPromotedInHome, Boolean isPromotedInSearches, Boolean hasAutomaticTokenRefresh, Set<TabResponseDto> tabs) {
        this.name = name;
        this.description = description;
        this.schedule = schedule;
        this.email = email;
        this.smallPhoto = smallPhoto;
        this.largePhoto = largePhoto;
        this.phone = phone;
        this.websiteLink = websiteLink;
        this.address = address;
        this.county = county;
        this.rating = rating;
        this.price = price;
        this.isPromotedInHome = isPromotedInHome;
        this.isPromotedInSearches = isPromotedInSearches;
        this.hasAutomaticTokenRefresh = hasAutomaticTokenRefresh;
        this.tabs = tabs;
    }

    public String getSchedule() {
        return schedule;
    }

    public StoreFrontRequestDto setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public StoreFrontRequestDto setCounty(String county) {
        this.county = county;
        return this;
    }

    public String getLargePhoto() {
        return largePhoto;
    }

    public StoreFrontRequestDto setLargePhoto(String largePhoto) {
        this.largePhoto = largePhoto;
        return this;
    }

    public String getName() {
        return name;
    }

    public StoreFrontRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StoreFrontRequestDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public StoreFrontRequestDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSmallPhoto() {
        return smallPhoto;
    }

    public StoreFrontRequestDto setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
        return this;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public StoreFrontRequestDto setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public StoreFrontRequestDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public StoreFrontRequestDto setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public StoreFrontRequestDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Boolean getPromotedInHome() {
        return isPromotedInHome;
    }

    public StoreFrontRequestDto setPromotedInHome(Boolean promotedInHome) {
        isPromotedInHome = promotedInHome;
        return this;
    }

    public Boolean getPromotedInSearches() {
        return isPromotedInSearches;
    }

    public StoreFrontRequestDto setPromotedInSearches(Boolean promotedInSearches) {
        isPromotedInSearches = promotedInSearches;
        return this;
    }

    public Set<TabResponseDto> getTabs() {
        return tabs;
    }

    public StoreFrontRequestDto setTabs(Set<TabResponseDto> tabs) {
        this.tabs = tabs;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public StoreFrontRequestDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Boolean getHasAutomaticTokenRefresh() {
        return hasAutomaticTokenRefresh;
    }

    public StoreFrontRequestDto setHasAutomaticTokenRefresh(Boolean hasAutomaticTokenRefresh) {
        this.hasAutomaticTokenRefresh = hasAutomaticTokenRefresh;
        return this;
    }
}
