package com.halfacode.ecommMaster.repositories;


import com.halfacode.ecommMaster.models.Transaction;
import com.halfacode.ecommMaster.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}

