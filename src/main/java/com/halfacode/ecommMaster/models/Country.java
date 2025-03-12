package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "countries")
public class Country{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states;

    public Country() {

    }


    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
    }


}