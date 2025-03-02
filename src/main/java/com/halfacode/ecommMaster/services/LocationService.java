package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.GeoLocationResponse;
import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.repositories.LocationCodeRepository;
import com.halfacode.ecommMaster.repositories.LocationRepository;
import com.halfacode.ecommMaster.repositories.ProductInventoryRepository;
import com.halfacode.ecommMaster.repositories.UserLocationRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {


    private final UserLocationRepository userLocationRepo;
    private final LocationRepository locationRepository;
    private final LocationCodeRepository locationCodeRepository;
    private final ProductInventoryRepository productInventoryRepository;
    public LocationService(UserLocationRepository userLocationRepo, LocationRepository locationRepository, LocationCodeRepository locationCodeRepository, ProductInventoryRepository productInventoryRepository) {
        this.userLocationRepo = userLocationRepo;
        this.locationRepository = locationRepository;
        this.locationCodeRepository = locationCodeRepository;
        this.productInventoryRepository = productInventoryRepository;
    }

    public Location findNearestLocation(Address address) {
        // Use geocoding APIs or predefined mappings for nearest location
        return locationRepository.findNearestByCountryAndCity(address.getCountry(), address.getCity());
    }
    public Optional<ProductInventory> checkStock(Product product, Location location) {
        return productInventoryRepository.findByProductAndLocation(product, location);
    }
    public void sellProduct(Product product, Location location, int quantity) {
        ProductInventory inventory = productInventoryRepository
                .findByProductAndLocation(product, location)
                .orElseThrow(() -> new IllegalStateException("No inventory found!"));

        inventory.reduceStock(quantity);
        productInventoryRepository.save(inventory);
    }
    public List<Location> getAvailableLocationsForProduct(Product product) {
        List<ProductInventory> inventories = productInventoryRepository.findAvailableInventories(product);
        return inventories.stream()
                .map(ProductInventory::getLocation)
                .collect(Collectors.toList());
    }

    public LocationCode addLocation(LocationCode locationCode){
        return locationCodeRepository.save(locationCode);
    }

    public UserLocation saveUserLocation(String userName, String lat, String longitude) throws ParseException, ParseException {
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read("POINT (" + longitude + " " + lat + ")");

        UserLocation userLocation = new UserLocation();
        userLocation.setPoint(geometry);
        userLocation.setUserName(userName);

        userLocationRepo.save(userLocation);

        return userLocation;
    }

    public Location saveLocation(GeoLocationResponse geoResponse) {
        Location location = new Location();
        location.setName(geoResponse.getCity());
        location.setCountry(geoResponse.getCountry());
        location.setCity(geoResponse.getCity());
        location.setLatitude(geoResponse.getLat());
        location.setLongitude(geoResponse.getLon());
        return locationRepository.save(location);
    }
}
