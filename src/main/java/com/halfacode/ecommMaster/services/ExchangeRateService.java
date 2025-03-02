package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.ExchangeRateDto;
import com.halfacode.ecommMaster.models.ExchangeRate;
import com.halfacode.ecommMaster.repositories.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
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

    public ExchangeRate addExchangeRate(ExchangeRateDto exchangeRateDto) {
        // Convert DTO to Entity
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setFromCurrency(exchangeRateDto.getFromCurrency());
        exchangeRate.setToCurrency(exchangeRateDto.getToCurrency());
        exchangeRate.setRate(exchangeRateDto.getRate());
        exchangeRate.setActive(exchangeRateDto.isActive());

        // Save the entity to the database
        return exchangeRateRepository.save(exchangeRate);
    }
}

