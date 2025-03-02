package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.MatchingUserDetailsDTO;
import com.halfacode.ecommMaster.models.LocationCode;
import com.halfacode.ecommMaster.models.UserLocation;
import com.halfacode.ecommMaster.services.FindNearestUserService;
import com.halfacode.ecommMaster.services.LocationService;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/v1/location")
@RestController
public class LocationController {

    private final LocationService locationService;


    private final FindNearestUserService nearestService;

    public LocationController(LocationService locationService, FindNearestUserService nearestService) {
        this.locationService = locationService;
        this.nearestService = nearestService;
    }
    @PostMapping
    public ResponseEntity<LocationCode> addLocation(@RequestBody LocationCode locationCode){
        LocationCode addLocation =locationService.addLocation(locationCode);
        URI uri= URI.create("/API/V1/location/" +locationCode.getCode());

        return ResponseEntity.created(uri).body(addLocation);
    }

    @PostMapping("/saveUserLocation")
    public String saveLocation(@RequestParam String userName, @RequestParam String lat,
                               @RequestParam String longitude) throws ParseException, ParseException {
        UserLocation userLocation = locationService.saveUserLocation(userName, lat, longitude);

        System.out.println("User location details saved successfully with id::" + userLocation.getId());

        return "User location details saved successfully.";
    }

    @GetMapping("/findNearestLocation")
    public List<MatchingUserDetailsDTO> findNearestLocation(String userName) {
        return nearestService.findNearestlocatio(userName);
    }
}
