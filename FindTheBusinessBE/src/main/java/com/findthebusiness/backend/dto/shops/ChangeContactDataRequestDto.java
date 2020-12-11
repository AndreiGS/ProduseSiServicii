package com.findthebusiness.backend.dto.shops;

public class ChangeContactDataRequestDto {

    private String address;
    private String phone;
    private String email;
    private String websiteLink;
    private String county;

    public ChangeContactDataRequestDto() {
    }

    public ChangeContactDataRequestDto(String address, String phone, String email, String websiteLink, String county) {
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.websiteLink = websiteLink;
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public ChangeContactDataRequestDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public ChangeContactDataRequestDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ChangeContactDataRequestDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public ChangeContactDataRequestDto setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public ChangeContactDataRequestDto setCounty(String county) {
        this.county = county;
        return this;
    }
}
