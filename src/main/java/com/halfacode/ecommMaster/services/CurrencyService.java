package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.GeoLocationResponse;
import com.halfacode.ecommMaster.models.Currency;
import com.halfacode.ecommMaster.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final GeoLocationService geoLocationService;

    public CurrencyService(CurrencyRepository currencyRepository, GeoLocationService geoLocationService) {
        this.currencyRepository = currencyRepository;
        this.geoLocationService = geoLocationService;
    }

    public String getCurrencyByCountry(String ip) {
        GeoLocationResponse location = geoLocationService.getGeoLocation(ip);

        if (location == null || !"success".equals(location.getStatus())) {
            return "USD"; // Default currency
        }

        return currencyRepository.findByCountry(location.getCountry())
                .map(Currency::getCurrencyCode)
                .orElse("USD");
    }
}
