package com.spring.kodo.repository;

import com.spring.kodo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
    Boolean existsByStripeTransactionId(String stripeTransactionId);
}
