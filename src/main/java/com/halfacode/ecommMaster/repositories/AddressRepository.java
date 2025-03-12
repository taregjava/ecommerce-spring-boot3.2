package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Address;
import com.halfacode.ecommMaster.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    Address findByUserAndDefaultAddressTrue(User user);
}
