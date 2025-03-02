package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.GeoLocationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GeoLocationService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "http://ip-api.com/json/";
    private static final List<String> SUPPORTED_COUNTRIES = Arrays.asList("Sudan", "Saudi Arabia");
    public GeoLocationResponse getGeoLocation(String ip) {
        if (ip == null || ip.isEmpty() || isLocalIp(ip)) {
            ip = getPublicIp(); // Fetch the public IP if it's local
        }

        Random random = new Random();
        String url = API_URL + ip + "?fields=status,country,city,lat,lon,zip,message&nocache=1&r=" + random.nextInt(100000);

        GeoLocationResponse response = restTemplate.getForObject(url, GeoLocationResponse.class);
        System.out.println("Fetched data from IP-API: " + response);

        return response;
    }

    private boolean isLocalIp(String ip) {
        return "0:0:0:0:0:0:0:1".equals(ip) || ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("127.") || ip.startsWith("172.");
    }

    private String getPublicIp() {
        try {
            return restTemplate.getForObject("https://api64.ipify.org?format=text", String.class);
        } catch (Exception e) {
            System.err.println("Failed to fetch public IP: " + e.getMessage());
            return "8.8.8.8"; // Default to Google's DNS
        }
    }
    public GeoLocationResponse getGeoLocationUnSupportCountry(String ip) {
        if (ip == null || ip.isEmpty() || isLocalIp(ip)) {
            ip = getPublicIp(); // Fetch the public IP if it's local
        }

        Random random = new Random();
        String url = API_URL + ip + "?fields=status,country,city,lat,lon,zip,message&nocache=1&r=" + random.nextInt(100000);

        GeoLocationResponse response = restTemplate.getForObject(url, GeoLocationResponse.class);

        // Debugging: Print API response
        System.out.println("Fetched data from IP-API: " + response);

        // Check if the user's country is supported
        if (response != null && "success".equals(response.getStatus())) {
            if (!SUPPORTED_COUNTRIES.contains(response.getCountry())) {
                System.out.println("User from unsupported country: " + response.getCountry());
                response.setMessage("Service not available in your country");
            }
        }

        return response;
    }

    public GeoLocationResponse getGeoLocationForRegistration(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr(); // Get IP if not behind a proxy
        }
        return getGeoLocation(ip); // Use existing method to avoid duplication
    }
}
