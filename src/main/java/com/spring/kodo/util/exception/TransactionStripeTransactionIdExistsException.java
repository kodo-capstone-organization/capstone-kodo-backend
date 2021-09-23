package com.spring.kodo.util.exception;

public class TransactionStripeTransactionIdExistsException extends Exception
{
    public TransactionStripeTransactionIdExistsException()
    {
    }

    public TransactionStripeTransactionIdExistsException(String msg)
    {
        super(msg);
    }
}
