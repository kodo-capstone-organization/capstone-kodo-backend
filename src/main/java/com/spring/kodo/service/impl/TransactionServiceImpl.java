package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Transaction;
import com.spring.kodo.repository.TransactionRepository;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.TransactionService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.*;

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
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
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

    public List<Transaction> getAllPlatformTransactions(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.findAll();
        }
        else
            {
            throw new AccountPermissionDeniedException("You do not have administrative rights to deactivate accounts.");
        }
    }

    @Override
    public List<Transaction> getAllPaymentsByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        return this.transactionRepository.getAllTransactionByPayerId(requestingAccount.getAccountId());
    }

    @Override
    public BigDecimal getLifetimeTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        return this.transactionRepository.getLifetimeEarningsByPayeeId(requestingAccount.getAccountId()).orElse(new BigDecimal(0));
    }

    @Override
    public List<Map<String, String>> getLifetimeEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException {

        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        List<Course> myCourses = requestingAccount.getCourses();

        // return format: [{'courseId': '5', 'courseName': 'Some Course', 'lifetimeEarnings': '18.50'}, {...}, ...]
        List<Map<String, String>> outputList = new ArrayList<>();
        for (Course c: myCourses)
        {
            BigDecimal lifetimeEarningFromCourse = this.transactionRepository.getLifetimeTutorPayoutByCourseId(c.getCourseId()).orElse(new BigDecimal(0));
            Map<String,String> inputData = new HashMap<>();
            inputData.put("courseId", c.getCourseId().toString());
            inputData.put("courseName", c.getName());
            inputData.put("lifetimeEarnings", lifetimeEarningFromCourse.toString());
            outputList.add(inputData);
        }
        return outputList;
    }

    @Override
    public BigDecimal getCurrentMonthTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        return this.transactionRepository.getCurrentMonthEarningsByPayeeId(requestingAccount.getAccountId()).orElse(new BigDecimal(0));
    }

    @Override
    public List<Map<String, String>> getCurrentMonthEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException {

        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        List<Course> myCourses = requestingAccount.getCourses();

        // return format: [{'courseId': '5', 'courseName': 'Some Course', 'lifetimeEarnings': '18.50'}, {...}, ...]
        List<Map<String, String>> outputList = new ArrayList<>();
        for (Course c: myCourses)
        {
            BigDecimal lifetimeEarningFromCourse = this.transactionRepository.getCurrentMonthTutorPayoutByCourseId(c.getCourseId()).orElse(new BigDecimal(0));
            Map<String,String> inputData = new HashMap<>();
            inputData.put("courseId", c.getCourseId().toString());
            inputData.put("courseName", c.getName());
            inputData.put("lifetimeEarnings", lifetimeEarningFromCourse.toString());
            outputList.add(inputData);
        }
        return outputList;
    }

}
