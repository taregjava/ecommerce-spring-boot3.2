package com.halfacode.ecommMaster.dto;

import com.halfacode.ecommMaster.models.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressShippingDTO {

    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
    private String country;
    private boolean defaultAddress;
    private Long userId; // Only return userId instead of full User object

    public AddressShippingDTO(Address address) {
        this.id = address.getId();
        this.addressLine1 = address.getAddressLine1();
        this.addressLine2 = address.getAddressLine2();
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getPostalCode();
        this.phoneNumber = address.getPhoneNumber();
        this.country = address.getCountry();
        this.defaultAddress = address.isDefaultAddress();
        this.userId = address.getUser().getId();
    }

}
