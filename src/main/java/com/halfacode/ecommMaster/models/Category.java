package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> products;

    public Category(){

    }
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "'}";
    }

}
