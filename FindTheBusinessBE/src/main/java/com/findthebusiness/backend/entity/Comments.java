package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Comments {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private byte[] email;
    private String text;
    private Double rating;
    private String postDate;

    @ManyToOne
    private Shops shop;

    //CONSTRUCTORS
    public Comments() {
    }

    //GETTERS/SETTERS
    public String getId() {
        return id;
    }

    public Comments setId(String id) {
        this.id = id;
        return this;
    }

    public Shops getShop() {
        return shop;
    }

    public Comments setShop(Shops shop) {
        this.shop = shop;
        return this;
    }

    public byte[] getEmail() {
        return email;
    }

    public Comments setEmail(byte[] email) {
        this.email = email;
        return this;
    }

    public String getText() {
        return text;
    }

    public Comments setText(String text) {
        this.text = text;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public Comments setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getPostDate() {
        return postDate;
    }

    public Comments setPostDate(String postDate) {
        this.postDate = postDate;
        return this;
    }
}
