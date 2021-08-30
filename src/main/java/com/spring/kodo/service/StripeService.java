package com.spring.kodo.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;

import java.math.BigDecimal;

public interface StripeService
{
    Account createNewStripeAccount() throws StripeException;

    AccountLink createStripeAccountLink(Account account) throws StripeException;

    PaymentIntent createPaymentIntent(String connectedStripeAccountId, BigDecimal amount) throws StripeException;
}
