package com.spring.kodo.controller;

import com.spring.kodo.restentity.request.StripePaymentReq;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.StripeService;
import com.spring.kodo.util.exception.*;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

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
            if (account.getStripeAccountId() == null)
            {
                Account stripeAccount = this.stripeService.createNewStripeAccount();
                AccountLink accountLink = this.stripeService.createStripeAccountLink(stripeAccount);

                account.setStripeAccountId(stripeAccount.getId());
                accountService.updateAccount(account, null, null, null, null, null, null, null);
                return ResponseEntity.status(HttpStatus.OK).body(accountLink.getUrl());
            }
            throw new AccountExistsException("User has an existing Stripe account connected to his Kodo account");
        }
        catch (StripeException | AccountExistsException | InputDataValidationException | TagNameExistsException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (AccountNotFoundException | TagNotFoundException | UpdateAccountException | EnrolledCourseNotFoundException | StudentAttemptNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (UnknownPersistenceException ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping("/createStripeSession")
    public ResponseEntity createStripeSession(@RequestPart(name="stripePaymentReq", required=true) StripePaymentReq stripePaymentReq)
    {
        try
        {
            String url = this.stripeService.createStripeSession(stripePaymentReq);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        }
        catch (StripeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/successfulStripeCheckout")
    public ResponseEntity successfulStripeCheckout(@RequestBody(required=true) String payload, HttpServletRequest request)
    {
        try
        {
            stripeService.handleSuccessfulStripeCheckout(payload, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (SignatureVerificationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (AccountNotFoundException | CourseNotFoundException | UpdateAccountException | CreateNewEnrolledCourseException | EnrolledCourseNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (InputDataValidationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
