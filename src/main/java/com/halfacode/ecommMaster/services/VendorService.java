package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.errors.VendorNotFoundException;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.Vendor;
import com.halfacode.ecommMaster.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public Vendor registerVendor(String name, String contactInfo, double commissionRate) {
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setContactInfo(contactInfo);
        vendor.setCommissionRate(commissionRate);
        return vendorRepository.save(vendor);
    }

    public List<Product> getVendorProducts(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("vendor not found "+vendorId ));
        return vendor.getProducts();
    }
}
