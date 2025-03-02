package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.GeoLocationResponse;
import com.halfacode.ecommMaster.services.GeoLocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/location")
public class GeoLocationController {

    private final GeoLocationService geoLocationService;

    public GeoLocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping("/get")
    public GeoLocationResponse getLocation(@RequestParam(required = false) String ip, HttpServletRequest request) {
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Forwarded-For"); // Get real IP from proxy
            if (ip == null || ip.isEmpty()) {
                ip = request.getRemoteAddr(); // Get direct request IP
            }
        }

        System.out.println("Using IP for lookup: " + ip);
        return geoLocationService.getGeoLocation(ip);
    }
    @GetMapping("/getSupport")
    public GeoLocationResponse getGeoLocationUnSupportCountry(@RequestParam(required = false) String ip, HttpServletRequest request) {
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Forwarded-For"); // Get real IP from proxy
            if (ip == null || ip.isEmpty()) {
                ip = request.getRemoteAddr(); // Get direct request IP
            }
        }

        System.out.println("Using IP for lookup: " + ip);
        GeoLocationResponse response = geoLocationService.getGeoLocationUnSupportCountry(ip);

        // If the country is not supported, return a fail response
        if (response != null && response.getMessage() != null) {
            return new GeoLocationResponse("fail", null, null, 0.0, 0.0, null, response.getMessage());
        }

        return response;
    }
}