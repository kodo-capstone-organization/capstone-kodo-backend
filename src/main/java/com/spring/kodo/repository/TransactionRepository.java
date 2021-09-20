package com.spring.kodo.repository;

import com.spring.kodo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
    Boolean existsByStripeTransactionId(String stripeTransactionId);

    @Query(value = "SELECT * FROM Transaction t", nativeQuery = true)
    BigDecimal getAllPlatformEarning();
}
