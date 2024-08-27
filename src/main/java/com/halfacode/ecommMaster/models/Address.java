package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address extends AbstractAddressWithCountry {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User customer;

    @Column(name = "default_address")
    private boolean defaultForShipping;

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public boolean isDefaultForShipping() {
        return defaultForShipping;
    }

    public void setDefaultForShipping(boolean defaultForShipping) {
        this.defaultForShipping = defaultForShipping;
    }

}
