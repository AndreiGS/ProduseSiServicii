package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Items {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String photo;

    private String title;
    private String description;
    private String price;

    @ManyToOne
    private Tabs tabs;

    @ManyToOne
    private Shops shop;

    //CONSTRUCTORS
    public Items() {
    }

    //GETTERS/SETTERS
    public String getId() {
        return id;
    }

    public Items setId(String id) {
        this.id = id;
        return this;
    }

    public Shops getShop() {
        return shop;
    }

    public Items setShop(Shops shop) {
        this.shop = shop;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Items setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Items setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Items setDescription(String description) {
        this.description = description;
        return this;
    }

    public Tabs getTabs() {
        return tabs;
    }

    public Items setTabs(Tabs tabs) {
        this.tabs = tabs;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Items setPrice(String price) {
        this.price = price;
        return this;
    }
}
