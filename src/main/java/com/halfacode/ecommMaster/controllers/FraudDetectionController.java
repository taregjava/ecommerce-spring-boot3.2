package com.halfacode.ecommMaster.controllers;



import com.halfacode.ecommMaster.models.Transaction;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.services.FraudDetectionService;
import com.halfacode.ecommMaster.services.TransactionService;
import com.halfacode.ecommMaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud-detection")
public class FraudDetectionController {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkTransaction(@RequestBody Transaction transaction) {
        boolean isFraudulent = fraudDetectionService.isFraudulentTransaction(transaction);
        return ResponseEntity.ok(isFraudulent);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId) {
        // Fetch the user by ID
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Get transactions for the user
        List<Transaction> transactions = transactionService.getTransactionsByUser(user);
        return ResponseEntity.ok(transactions);
    }
}
