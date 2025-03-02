package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.ExchangeRate;
import com.halfacode.ecommMaster.repositories.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyConverterService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    String apiKey = "b307be2b0d82c5f661a8a732696f2939";
    String apiUrl = "https://api.exchangeratesapi.io/latest?access_key=" + apiKey;
    // private final String BASE_URL = "http://data.fixer.io/api/latest?access_key=" + API_KEY;
   private final String BASE_URL = "https://api.exchangerate.host/latest";
    @Autowired
    private ExchangeRateService exchangeRateService;

    public double convert(double amount, String fromCurrency, String toCurrency) {
        double rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return amount * rate;
    }
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> body = response.getBody();

            // Ensure "rates" key exists
            if (!body.containsKey("rates")) {
                throw new RuntimeException("Rates data is missing from the API response");
            }

            Map<String, Object> rates = (Map<String, Object>) body.get("rates");

            // Ensure both currencies exist in the rates map
            if (!rates.containsKey(fromCurrency) || !rates.containsKey(toCurrency)) {
                throw new RuntimeException("Currency conversion rates not available for " + fromCurrency + " or " + toCurrency);
            }

            BigDecimal fromRate = new BigDecimal(rates.get(fromCurrency).toString());
            BigDecimal toRate = new BigDecimal(rates.get(toCurrency).toString());

            return amount.multiply(toRate).divide(fromRate, 2, RoundingMode.HALF_UP);
        }

        throw new RuntimeException("Unable to fetch exchange rates");
    }
    public BigDecimal convertCurrencydB(BigDecimal amount, String fromCurrency, String toCurrency) {
        // Fetch the exchange rate from the database
        ExchangeRate exchangeRate = exchangeRateRepository
                .findByFromCurrencyAndToCurrency(fromCurrency, toCurrency)
                .orElseThrow(() -> new RuntimeException("Currency conversion rate not found"));

        // Multiply the amount by the exchange rate
        return amount.multiply(exchangeRate.getRate());
    }
}
