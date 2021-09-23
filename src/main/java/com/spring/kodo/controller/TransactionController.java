package com.spring.kodo.controller;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.restentity.response.TutorCourseEarningsResp;
import com.spring.kodo.service.inter.TransactionService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.AccountPermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController
{
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionService transactionService;

    @GetMapping("/getAllPlatformTransactions/{accountId}")
    public List<Transaction> getAllPlatformTransactions(@PathVariable Long accountId)
    {
        try
        {
            // Must be admin to be able to perform this
            return this.transactionService.getAllPlatformTransactions(accountId);
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllPaymentsByAccountId/{accountId}")
    public List<Transaction> getAllPaymentsByAccountId(@PathVariable Long accountId)
    {
        try
        {
            return this.transactionService.getAllPaymentsByAccountId(accountId);
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseEarningsPageDataByAccountId/{accountId}")
    public TutorCourseEarningsResp getCourseEarningsPageDataByAccountId(@PathVariable Long accountId)
    {
        try
        {
            BigDecimal lifetimeTotalEarnings = this.transactionService.getLifetimeTotalEarningsByAccountId(accountId);
            List<Map<String, String>> lifetimeEarningsByCourse = this.transactionService.getLifetimeEarningsByCourseByAccountId(accountId);
            BigDecimal currentMonthTotalEarnings = this.transactionService.getCurrentMonthTotalEarningsByAccountId(accountId);
            List<Map<String, String>> currentMonthEarningsByCourse = this.transactionService.getCurrentMonthEarningsByCourseByAccountId(accountId);

            TutorCourseEarningsResp responseObj = new TutorCourseEarningsResp();
            responseObj.setLifetimeTotalEarnings(lifetimeTotalEarnings);
            responseObj.setLifetimeEarningsByCourse(lifetimeEarningsByCourse);
            responseObj.setCurrentMonthTotalEarnings(currentMonthTotalEarnings);
            responseObj.setCurrentMonthEarningsByCourse(currentMonthEarningsByCourse);

            return responseObj;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
