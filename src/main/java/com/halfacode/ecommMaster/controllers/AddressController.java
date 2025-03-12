package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.AddressShippingDTO;
import com.halfacode.ecommMaster.models.Address;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.services.AddressService;
import com.halfacode.ecommMaster.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(addressService.getUserAddresses(user));
    }

    @PostMapping("/save")
    public ResponseEntity<AddressShippingDTO> saveAddress(@RequestParam Long userId, @RequestBody Address address) {
        User user = userService.getUserById(userId);
        address.setUser(user);
        Address savedAddress = addressService.saveAddress(address,user);
        return ResponseEntity.ok(new AddressShippingDTO(savedAddress));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        addressService.deleteAddress(id, user);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
