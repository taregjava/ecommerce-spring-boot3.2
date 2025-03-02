package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.ExchangeRateDto;
import com.halfacode.ecommMaster.models.ExchangeRate;
import com.halfacode.ecommMaster.services.CurrencyConverterService;
import com.halfacode.ecommMaster.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyConverterController {

    @Autowired
    private CurrencyConverterService currencyConverterService;
    @Autowired
    private ExchangeRateService exchangeRateService;
    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        BigDecimal convertedAmount = currencyConverterService.convertCurrency(amount, fromCurrency, toCurrency);

        Map<String, Object> response = new HashMap<>();
        response.put("amount", amount);
        response.put("from", fromCurrency);
        response.put("to", toCurrency);
        response.put("convertedAmount", convertedAmount);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/convertdb")
    public ResponseEntity<Map<String, Object>> convertCurrencydB(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        BigDecimal convertedAmount = currencyConverterService.convertCurrencydB(amount, fromCurrency, toCurrency);

        Map<String, Object> response = new HashMap<>();
        response.put("amount", amount);
        response.put("from", fromCurrency);
        response.put("to", toCurrency);
        response.put("convertedAmount", convertedAmount);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-exchange-rate")
    public ResponseEntity<ExchangeRate> addExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto) {
        // Add the exchange rate to the database
        ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(exchangeRateDto);
        return ResponseEntity.ok(exchangeRate);
    }
}
