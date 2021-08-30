package com.spring.kodo.controller;

import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.StripeService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping(path="/stripe")
public class StripeController
{
    @Autowired
    AccountService accountService;

    @Autowired
    StripeService stripeService;

    @PostMapping("/createStripeAccount")
    public ResponseEntity createStripeAccount(@RequestPart(name="accountId", required=true) Long accountId)
    {
        try
        {
            com.spring.kodo.entity.Account account = this.accountService.getAccountByAccountId(accountId);
            if (account.getStripeAccountId() != null)
            {
                return ResponseEntity.status(HttpStatus.OK).body(account.getStripeAccountId());
            }
            Account stripeAccount = this.stripeService.createNewStripeAccount();
            AccountLink accountLink = this.stripeService.createStripeAccountLink(stripeAccount);
            return ResponseEntity.status(HttpStatus.OK).body(stripeAccount.getId());
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (StripeException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error with Stripe Usage");
        }
    }

    @PostMapping("/createPaymentIntent")
    public ResponseEntity createPaymentIntent(@RequestPart(name="tutorStripeAccountId", required=true) String tutorStripeAccountId,
                                              @RequestPart(name="amount", required=true) BigDecimal amount)
    {
        try
        {
            PaymentIntent paymentIntent = this.stripeService.createPaymentIntent(tutorStripeAccountId, amount);
            return ResponseEntity.status(HttpStatus.OK).body(paymentIntent.getClientSecret());
        } catch (StripeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error with Stripe Usage");
        }
    }
}