package com.halfacode.ecommMaster.services;
import com.halfacode.ecommMaster.models.Transaction;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

        @Autowired
        private TransactionRepository transactionRepository;

        public List<Transaction> getTransactionsByUser(User user) {
            return transactionRepository.findByUser(user);
        }

        public Transaction saveTransaction(Transaction transaction) {
            return transactionRepository.save(transaction);
        }
}
