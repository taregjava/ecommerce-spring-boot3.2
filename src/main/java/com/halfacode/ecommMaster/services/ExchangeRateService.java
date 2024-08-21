package com.halfacode.ecommMaster.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExchangeRateService {

    // Simulate fetching exchange rates from an external service
    public double getExchangeRate(String fromCurrency, String toCurrency) {
        // In a real application, this would fetch live rates
        Map<String, Double> exchangeRates = Map.of(
                "USD_TO_SAR", 3.75,
                "EUR_TO_SAR", 4.10,
                "GBP_TO_SAR", 4.50
        );

        return exchangeRates.getOrDefault(fromCurrency + "_TO_" + toCurrency, 1.0);
    }
}

