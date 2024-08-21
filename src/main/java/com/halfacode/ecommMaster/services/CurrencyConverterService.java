package com.halfacode.ecommMaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConverterService {

    @Autowired
    private ExchangeRateService exchangeRateService;

    public double convert(double amount, String fromCurrency, String toCurrency) {
        double rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return amount * rate;
    }
}
