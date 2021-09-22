package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.util.exception.*;
import java.util.List;

public interface TransactionService
{
    Transaction createNewTransaction(Transaction newTransaction, Long payerId, Long payeeId, Long courseId) throws InputDataValidationException, TransactionStripeTransactionIdExistsException, UnknownPersistenceException, CourseNotFoundException, AccountNotFoundException;

    Boolean isAccountExistsByStripeTransactionId(String stripeTransactionId);

    List<Transaction> getAllPlatformTransactions(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    List<Transaction> getAllPaymentsByAccountId(Long requestingAccountId) throws AccountNotFoundException;
}
