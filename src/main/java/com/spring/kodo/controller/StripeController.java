package com.spring.kodo.controller;

import com.spring.kodo.restentity.StripePaymentReq;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.StripeService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/stripe")
public class StripeController
{
    @Autowired
    AccountService accountService;

    @Autowired
    StripeService stripeService;

    @PostMapping("/createStripeAccount")
    public ResponseEntity createStripeAccount(@RequestParam(name="accountId", required=true) Long accountId)
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
        } catch (StripeException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/createStripeSession")
    public ResponseEntity createStripeSession(@RequestPart(name="stripePaymentReq", required=true) StripePaymentReq stripePaymentReq)
    {
        try
        {
            String url = this.stripeService.createStripeSession(stripePaymentReq);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (StripeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
