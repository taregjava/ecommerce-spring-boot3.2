package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Transaction;
import com.halfacode.ecommMaster.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraudDetectionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public boolean isFraudulentTransaction(Transaction transaction) {
        // Example: Check for unusually high transaction amounts
        if (transaction.getAmount() > 10000) {
            return true;
        }

        // Example: Check for transactions from unusual locations
        if (!isLocationValid(transaction.getLocation())) {
            return true;
        }

        return false;
    }

    private boolean isLocationValid(String location) {
        // Placeholder for actual location validation logic
        return true; // Assume all locations are valid for simplicity
    }
}
