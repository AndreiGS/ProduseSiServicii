package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Pages {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private Integer promotedShopsNo;

    //CONSTRUCTOR
    public Pages() {
    }

    //GETTERS/SETTERS

    public String getId() {
        return id;
    }

    public Pages setId(String id) {
        this.id = id;
        return this;
    }

    public Integer getPromotedShopsNo() {
        return promotedShopsNo;
    }

    public Pages setPromotedShopsNo(Integer promotedShopsNo) {
        this.promotedShopsNo = promotedShopsNo;
        return this;
    }
}
