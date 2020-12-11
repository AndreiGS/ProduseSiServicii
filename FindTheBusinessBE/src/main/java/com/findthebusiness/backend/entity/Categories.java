package com.findthebusiness.backend.entity;

import java.util.List;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Categories {
    //PROPERTIES
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String name;


    //CONSTRUCTORS
    public Categories() {
    }

    //GETTERS/SETTERS
    public String getId() {
        return id;
    }

    public Categories setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Categories setName(String name) {
        this.name = name;
        return this;
    }
}
