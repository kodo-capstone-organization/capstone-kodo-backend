package com.spring.kodo.controller;

import com.spring.kodo.service.inter.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController
{
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionService transactionService;

    @GetMapping("/getAllPlatformEarning")
    public BigDecimal getAllPlatformEarning()
    {
        return this.transactionService.getAllPlatformEarning();
    }
}
