package com.findthebusiness.backend.entity;

import com.findthebusiness.backend.utils.enums.PriceEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Shops {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private String schedule;

    @NotNull
    private String smallPhoto;

    private String largePhoto;

    @NotNull
    private Boolean isPromotedInHome;

    private Date promotedDateInHome;
    private Integer promotedDaysInHome;

    @NotNull
    private Boolean isPromotedInSearches;

    private Date promotedDateInSearches;
    private Integer promotedDaysInSearches;

    @NotNull
    private Date boughtAt;

    @NotNull
    private Date refreshedAt;

    private Integer price;
    private Integer priceNumber;

    @NotNull
    private Boolean isPublished;

    @NotNull
    private Integer actualSize;

    @NotNull
    private Integer maximumSize;

    @NotNull
    private Boolean hasAutomaticTokenRefresh;

    private String email;
    private String phone;
    private String websiteLink;
    private String address;
    private String county;

    private Double rating;
    private Integer ratingNumber;

    @ManyToOne
    private Users user;
    @ManyToMany
    private List<Subcategories> subcategories;
    @OneToOne
    private Categories categories;
    @OneToMany(mappedBy = "shop", cascade = CascadeType.REMOVE)
    private List<Items> items;
    @OneToMany(mappedBy = "shops", cascade = CascadeType.REMOVE)
    private List<Tabs> tabs;
    @OneToMany(mappedBy = "shop", cascade = CascadeType.REMOVE)
    private List<Comments> comments;

    //CONSTRUCTORS
    public Shops() {
    }

    //GETTERS/SETTERS
    public String getId() {
        return id;
    }

    public Shops setId(String id) {
        this.id = id;
        return this;
    }

    public Users getUser() {
        return user;
    }

    public Shops setUser(Users user) {
        this.user = user;
        return this;
    }

    public String getName() {
        return name;
    }

    public Shops setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Shops setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Shops setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Shops setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSmallPhoto() {
        return smallPhoto;
    }

    public Shops setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
        return this;
    }

    public String getLargePhoto() {
        return largePhoto;
    }

    public Shops setLargePhoto(String largePhoto) {
        this.largePhoto = largePhoto;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Shops setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public Shops setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public Shops setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Integer getRatingNumber() {
        return ratingNumber;
    }

    public Shops setRatingNumber(Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
        return this;
    }

    public Boolean getPromotedInHome() {
        return isPromotedInHome;
    }

    public Shops setPromotedInHome(Boolean promotedInHome) {
        isPromotedInHome = promotedInHome;
        return this;
    }

    public Date getPromotedDateInHome() {
        return promotedDateInHome;
    }

    public Shops setPromotedDateInHome(Date promotedDateInHome) {
        this.promotedDateInHome = promotedDateInHome;
        return this;
    }

    public Integer getPromotedDaysInHome() {
        return promotedDaysInHome;
    }

    public Shops setPromotedDaysInHome(Integer promotedDaysInHome) {
        this.promotedDaysInHome = promotedDaysInHome;
        return this;
    }

    public Boolean getPromotedInSearches() {
        return isPromotedInSearches;
    }

    public Shops setPromotedInSearches(Boolean promotedInSearches) {
        isPromotedInSearches = promotedInSearches;
        return this;
    }

    public Date getPromotedDateInSearches() {
        return promotedDateInSearches;
    }

    public Shops setPromotedDateInSearches(Date promotedDateInSearches) {
        this.promotedDateInSearches = promotedDateInSearches;
        return this;
    }

    public Integer getPromotedDaysInSearches() {
        return promotedDaysInSearches;
    }

    public Shops setPromotedDaysInSearches(Integer promotedDaysInSearches) {
        this.promotedDaysInSearches = promotedDaysInSearches;
        return this;
    }

    public Date getBoughtAt() {
        return boughtAt;
    }

    public Shops setBoughtAt(Date boughtAt) {
        this.boughtAt = boughtAt;
        return this;
    }

    public Date getRefreshedAt() {
        return refreshedAt;
    }

    public Shops setRefreshedAt(Date refreshedAt) {
        this.refreshedAt = refreshedAt;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public Shops setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public Integer getPriceNumber() {
        return priceNumber;
    }

    public Shops setPriceNumber(Integer priceNumber) {
        this.priceNumber = priceNumber;
        return this;
    }

    public Categories getCategories() {
        return categories;
    }

    public Shops setCategories(Categories categories) {
        this.categories = categories;
        return this;
    }

    public List<Subcategories> getSubcategories() {
        return subcategories;
    }

    public Shops setSubcategories(List<Subcategories> subcategories) {
        this.subcategories = subcategories;
        return this;
    }

    public List<Items> getItems() {
        return items;
    }

    public Shops setItems(List<Items> items) {
        this.items = items;
        return this;
    }

    public List<Tabs> getTabs() {
        return tabs;
    }

    public Shops setTabs(List<Tabs> tabs) {
        this.tabs = tabs;
        return this;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public Shops setComments(List<Comments> comments) {
        this.comments = comments;
        return this;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public Shops setPublished(Boolean published) {
        isPublished = published;
        return this;
    }

    public Integer getActualSize() {
        return actualSize;
    }

    public Shops setActualSize(Integer actualSize) {
        this.actualSize = actualSize;
        return this;
    }

    public Integer getMaximumSize() {
        return maximumSize;
    }

    public Shops setMaximumSize(Integer maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public Boolean getHasAutomaticTokenRefresh() {
        return hasAutomaticTokenRefresh;
    }

    public Shops setHasAutomaticTokenRefresh(Boolean hasAutomaticTokenRefresh) {
        this.hasAutomaticTokenRefresh = hasAutomaticTokenRefresh;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public Shops setCounty(String county) {
        this.county = county;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public Shops setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }
}
