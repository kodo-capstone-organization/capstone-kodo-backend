package com.spring.kodo.service.impl;

import com.google.gson.Gson;
import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Transaction;
import com.spring.kodo.repository.TransactionRepository;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.TransactionService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.NowMonthYearUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
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

    // takes in payer's account id
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

        List<Map<String, String>> outputList = new ArrayList<>();
        for (Course c: myCourses)
        {
            BigDecimal lifetimeEarningFromCourse = this.transactionRepository.getLifetimeTutorPayoutByCourseId(c.getCourseId()).orElse(new BigDecimal(0));
            Map<String,String> inputData = new HashMap<>();
            inputData.put("courseId", c.getCourseId().toString());
            inputData.put("courseName", c.getName());
            inputData.put("courseNameWithEarnings", c.getName() + " ($" + lifetimeEarningFromCourse.toString() + ")");
            inputData.put("earnings", lifetimeEarningFromCourse.toString());
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

        // return format: [{'courseId': '5', 'courseName': 'Some Course', 'earnings': '18.50', 'courseNameWithEarnings': 'XXX $(YY)}' }, {...}, ...]
        List<Map<String, String>> outputList = new ArrayList<>();
        for (Course c: myCourses)
        {
            BigDecimal lifetimeEarningFromCourse = this.transactionRepository.getCurrentMonthTutorPayoutByCourseId(c.getCourseId()).orElse(new BigDecimal(0));
            Map<String,String> inputData = new HashMap<>();
            inputData.put("courseId", c.getCourseId().toString());
            inputData.put("courseName", c.getName());
            inputData.put("courseNameWithEarnings", c.getName() + " ($" + lifetimeEarningFromCourse.toString() + ")");
            inputData.put("earnings", lifetimeEarningFromCourse.toString());
            outputList.add(inputData);
        }
        return outputList;
    }

    @Override
    public List<Map<String, String>> getCourseStatsByMonthForLastYear(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        List<Course> myCourses = requestingAccount.getCourses();
        List<Map<String, String>> outputList = new ArrayList<>();

        for (Course c: myCourses)
        {
            Map<String,String> inputData = new HashMap<>();
            inputData.put("courseId", c.getCourseId().toString());
            inputData.put("courseName", c.getName());

            LocalDate today = LocalDate.now();
            LocalDate firstOfCurMonth = today.withDayOfMonth(1);
            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();
            List<Map<String, String>> data = new ArrayList<>();

            int iter = 12;
            while (iter > 0) {
                Map<String, String> dataInput = new HashMap<>();
                Month month = firstOfCurMonth.getMonth();
                int year = firstOfCurMonth.getYear();
                dataInput.put("monthyear", monthListShortName.get(month.getValue()-1) + " " + year); // SEP 2021
                BigDecimal tutorPayoutByMonthByCourseId = this.transactionRepository.getTutorPayoutByMonthByCourseId(c.getCourseId(), year, month.getValue()).orElse(new BigDecimal(0));
                dataInput.put("earnings", tutorPayoutByMonthByCourseId.toString());

                data.add(dataInput);
                iter--;
                firstOfCurMonth = firstOfCurMonth.minusMonths(1);
            }

            Gson gson = new Gson();
            inputData.put("data", gson.toJson(data)); // stringify the list
            outputList.add(inputData);
        }

        return outputList;
    }

    @Override
    public BigDecimal getLifetimePlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLifetimePlatformEarning();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getCurrentMonthPlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthPlatformEarning();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public Map<String, BigDecimal> getMonthlyPlatformEarningsForLastYear(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            LocalDate today = LocalDate.now();
            LocalDate currentMonth = today.withDayOfMonth(1);

            Map<String,BigDecimal> monthlyPlatformEarningsForLastYear = new HashMap<>();

            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();

            for (int i = 0; i < 12; i++)
            {
                int month = currentMonth.getMonth().getValue();
                int year = currentMonth.getYear();

                BigDecimal earningsForCurrentMonth = this.transactionRepository.getMonthlyPlatformEarning(year, month);

                monthlyPlatformEarningsForLastYear.put(monthListShortName.get(month-1) + " " + year, earningsForCurrentMonth);
            }

            return monthlyPlatformEarningsForLastYear;
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getLifetimeCourseEarning(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLifetimeCourseEarning(course.getCourseId());
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getCurrentMonthCourseEarning(Long requestingAccountId, Long courseId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthCourseEarning(course.getCourseId());
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public Map<String, BigDecimal> getMonthlyCourseEarningForLastYear(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            LocalDate today = LocalDate.now();
            LocalDate currentMonth = today.withDayOfMonth(1);

            Map<String,BigDecimal> monthlyCourseEarningsForLastYear = new HashMap<>();

            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();

            for (int i = 0; i < 12; i++)
            {
                int month = currentMonth.getMonth().getValue();
                int year = currentMonth.getYear();

                BigDecimal earningsForCurrentMonth = this.transactionRepository.getMonthlyCourseEarningForLastYear(course.getCourseId(), year, month);

                monthlyCourseEarningsForLastYear.put(monthListShortName.get(month-1) + " " + year, earningsForCurrentMonth);
            }

            return monthlyCourseEarningsForLastYear;
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }
}
