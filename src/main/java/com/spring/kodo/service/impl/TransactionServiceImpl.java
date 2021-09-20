package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Transaction;
import com.spring.kodo.repository.TransactionRepository;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.TransactionService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService
{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AccountService accountService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransactionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Transaction createNewTransaction(Transaction newTransaction, Long payerId, Long payeeId, Long courseId) throws InputDataValidationException, TransactionStripeTransactionIdExistsException, UnknownPersistenceException, CourseNotFoundException, AccountNotFoundException {
        try
        {
            Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(newTransaction);
            if (constraintViolations.isEmpty())
            {
                if (!isAccountExistsByStripeTransactionId(newTransaction.getStripeTransactionId()))
                {
                    Course course = courseService.getCourseByCourseId(courseId);
                    newTransaction.setCourse(course);

                    Account payerAccount = accountService.getAccountByAccountId(payerId);
                    newTransaction.setPayer(payerAccount);

                    Account payeeAccount = accountService.getAccountByAccountId(payeeId);
                    newTransaction.setPayee(payeeAccount);

                    transactionRepository.saveAndFlush(newTransaction);
                    return newTransaction;
                }
                else
                {
                    throw new TransactionStripeTransactionIdExistsException("Transaction with stripe transaction id " + newTransaction.getStripeTransactionId() + " already exists!");
                }
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public Boolean isAccountExistsByStripeTransactionId(String stripeTransactionId) {
        return transactionRepository.existsByStripeTransactionId(stripeTransactionId);
    }


}
