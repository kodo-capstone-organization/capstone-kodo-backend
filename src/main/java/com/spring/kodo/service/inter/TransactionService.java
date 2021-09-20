package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.util.exception.*;

import java.math.BigDecimal;

public interface TransactionService
{
    public Transaction createNewTransaction(Transaction newTransaction, Long payerId, Long payeeId, Long courseId) throws InputDataValidationException, TransactionStripeTransactionIdExistsException, UnknownPersistenceException, CourseNotFoundException, AccountNotFoundException;

    public Boolean isAccountExistsByStripeTransactionId(String stripeTransactionId);

    public BigDecimal getAllPlatformEarning(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;
}
