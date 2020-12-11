package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Users {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 8)
    private String password;

    @Column(unique = true)
    private byte[] email;

    @NotNull
    private Long balance;

    @NotNull
    private Boolean hasAddedBalance;

    @NotNull
    private String userRole;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isBanned;

    @NotNull
    private Integer smallTokens;

    @NotNull
    private Integer mediumTokens;

    @NotNull
    private Integer largeTokens;

    @NotNull
    private Integer unlimitedTokens;

    @NotNull
    private Integer noOfShopsAllowed;

    @NotNull
    private Integer noOfShopsInAccount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Shops> shops;

    //CONSTRUCTORS
    public Users() {
    }

    //GETTERS/SETTERS
    public String getId() {
        return id;
    }

    public Users setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Users setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }

    public byte[] getEmail() {
        return email;
    }

    public Users setEmail(byte[] email) {
        this.email = email;
        return this;
    }

    public String getUserRole() {
        return userRole;
    }

    public Users setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Users setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public Boolean getBanned() {
        return isBanned;
    }

    public Users setBanned(Boolean banned) {
        isBanned = banned;
        return this;
    }

    public Long getBalance() {
        return balance;
    }

    public Users setBalance(Long balance) {
        this.balance = balance;
        return this;
    }

    public Integer getSmallTokens() {
        return smallTokens;
    }

    public Users setSmallTokens(Integer smallTokens) {
        this.smallTokens = smallTokens;
        return this;
    }

    public Integer getMediumTokens() {
        return mediumTokens;
    }

    public Users setMediumTokens(Integer mediumTokens) {
        this.mediumTokens = mediumTokens;
        return this;
    }


    public Integer getLargeTokens() {
        return largeTokens;
    }

    public Users setLargeTokens(Integer largeTokens) {
        this.largeTokens = largeTokens;
        return this;
    }

    public Integer getUnlimitedTokens() {
        return unlimitedTokens;
    }

    public Users setUnlimitedTokens(Integer unlimitedTokens) {
        this.unlimitedTokens = unlimitedTokens;
        return this;
    }

    public List<Shops> getShops() {
        return shops;
    }

    public Users setShops(List<Shops> shops) {
        this.shops = shops;
        return this;
    }

    public Integer getNoOfShopsAllowed() {
        return noOfShopsAllowed;
    }

    public Users setNoOfShopsAllowed(Integer noOfShopsAllowed) {
        this.noOfShopsAllowed = noOfShopsAllowed;
        return this;
    }

    public Integer getNoOfShopsInAccount() {
        return noOfShopsInAccount;
    }

    public Users setNoOfShopsInAccount(Integer noOfShopsInAccount) {
        this.noOfShopsInAccount = noOfShopsInAccount;
        return this;
    }

    public Boolean getHasAddedBalance() {
        return hasAddedBalance;
    }

    public Users setHasAddedBalance(Boolean hasAddedBalance) {
        this.hasAddedBalance = hasAddedBalance;
        return this;
    }
}
