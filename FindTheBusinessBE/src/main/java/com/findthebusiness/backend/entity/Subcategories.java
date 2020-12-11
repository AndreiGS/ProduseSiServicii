package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Subcategories {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String name;

    @ManyToOne
    private Categories category;

    //CONSTRUCTORS
    public Subcategories() {
    }

    //GETTERS/SETTERS
    public String getId() {
        return id;
    }

    public Subcategories setId(String id) {
        this.id = id;
        return this;
    }

    public Categories getCategory() {
        return category;
    }

    public Subcategories setCategory(Categories category) {
        this.category = category;
        return this;
    }

    public String getName() {
        return name;
    }

    public Subcategories setName(String name) {
        this.name = name;
        return this;
    }
}
