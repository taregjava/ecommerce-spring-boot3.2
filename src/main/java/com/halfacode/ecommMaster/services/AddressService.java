package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Address;
import com.halfacode.ecommMaster.models.Country;
import com.halfacode.ecommMaster.models.Location;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.AddressRepository;
import com.halfacode.ecommMaster.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

    public Address getDefaultAddress(User user) {
        return addressRepository.findByUserAndDefaultAddressTrue(user);
    }

    @Transactional
    public Address saveAddress(Address address, User user) {
        // Set the user for the address
        address.setUser(user);

        // If the user has an associated location, use it
        if (user.getLocation() != null) {
            Location userLocation = user.getLocation();
            address.setCity(userLocation.getCity());
            address.setCountry(userLocation.getCountry());
            address.setPostalCode(userLocation.getPostalCode());
        }

        // If the address is marked as default, unset any other default addresses
        if (address.isDefaultAddress()) {
            List<Address> userAddresses = getUserAddresses(user);
            for (Address addr : userAddresses) {
                addr.setDefaultAddress(false);
            }
            addressRepository.saveAll(userAddresses);  // Save updated addresses
        }

        // Save the address
        return addressRepository.save(address);
    }


    @Transactional
    public void deleteAddress(Long addressId, User user) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!address.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to address");
        }
        addressRepository.delete(address);
    }
}
