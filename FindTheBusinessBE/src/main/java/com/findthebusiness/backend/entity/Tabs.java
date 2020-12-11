package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Tabs {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Column(length = 20)
    private String name;

    @ManyToOne
    private Shops shops;

    public Tabs() {
    }

    public String getId() {
        return id;
    }

    public Tabs setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tabs setName(String name) {
        this.name = name;
        return this;
    }

    public Shops getShops() {
        return shops;
    }

    public Tabs setShops(Shops shops) {
        this.shops = shops;
        return this;
    }
}
