package com.spring.kodo.controller;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.restentity.response.CourseEarningsResp;
import com.spring.kodo.restentity.response.PlatformEarningsResp;
import com.spring.kodo.restentity.response.TagEarningsResp;
import com.spring.kodo.restentity.response.TutorCourseEarningsResp;
import com.spring.kodo.service.inter.TransactionService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.AccountPermissionDeniedException;
import com.spring.kodo.util.exception.CourseNotFoundException;
import com.spring.kodo.util.exception.TagNotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    @GetMapping("/getTutorEarningsByAccountId/{accountId}")
    public BigDecimal getTutorEarningsByAccountId(@PathVariable Long accountId)
    {
        try
        {
            return this.transactionService.getLifetimeTotalEarningsByAccountId(accountId);
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
            List<Map<String, String>> courseStatsByMonthForLastYear = this.transactionService.getCourseStatsByMonthForLastYear(accountId);

            TutorCourseEarningsResp responseObj = new TutorCourseEarningsResp();
            responseObj.setLifetimeTotalEarnings(lifetimeTotalEarnings);
            responseObj.setLifetimeEarningsByCourse(lifetimeEarningsByCourse);
            responseObj.setCurrentMonthTotalEarnings(currentMonthTotalEarnings);
            responseObj.setCurrentMonthEarningsByCourse(currentMonthEarningsByCourse);
            responseObj.setCourseStatsByMonthForLastYear(courseStatsByMonthForLastYear);

            return responseObj;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getPlatformEarningsAdminData/{requestingAccountId}")
    public PlatformEarningsResp getPlatformEarningsAdminData(@PathVariable Long requestingAccountId)
    {
        try
        {
            BigDecimal lifetimePlatformEarnings = this.transactionService.getLifetimePlatformEarnings(requestingAccountId);
            BigDecimal currentMonthPlatformEarnings = this.transactionService.getCurrentMonthPlatformEarnings(requestingAccountId);
            Map<String, BigDecimal> monthlyPlatformEarningsForLastYear = this.transactionService.getMonthlyPlatformEarningsForLastYear(requestingAccountId);

            PlatformEarningsResp platformEarningsResp = new PlatformEarningsResp();
            platformEarningsResp.setLifetimePlatformEarnings(lifetimePlatformEarnings);
            platformEarningsResp.setCurrentMonthPlatformEarnings(currentMonthPlatformEarnings);
            platformEarningsResp.setMonthlyPlatformEarningsForLastYear(monthlyPlatformEarningsForLastYear);

            return platformEarningsResp;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/getCourseEarningsAdminData/{courseId}&{requestingAccountId}")
    public CourseEarningsResp getCourseEarningsAdminData(@PathVariable Long courseId, @PathVariable Long requestingAccountId)
    {
        try
        {
            BigDecimal lifetimeCourseEarnings = this.transactionService.getLifetimeCourseEarning(requestingAccountId, courseId);
            BigDecimal currentMonthCourseEarnings = this.transactionService.getCurrentMonthCourseEarning(requestingAccountId, courseId);
            Map<String, BigDecimal> monthlyCourseEarningsForLastYear = this.transactionService.getMonthlyCourseEarningForLastYear(requestingAccountId, courseId);

            CourseEarningsResp courseEarningsResp = new CourseEarningsResp();
            courseEarningsResp.setLifetimeCourseEarning(lifetimeCourseEarnings);
            courseEarningsResp.setCurrentMonthCourseEarning(currentMonthCourseEarnings);
            courseEarningsResp.setMonthlyCourseEarningForLastYear(monthlyCourseEarningsForLastYear);

            return courseEarningsResp;
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/getTagEarningsAdminData/{tagId}&{requestingAccountId}")
    public TagEarningsResp getTagEarningsAdminData(@PathVariable Long tagId, @PathVariable Long requestingAccountId)
    {
        try
        {
            BigDecimal lifetimeTagEarnings = this.transactionService.getLifetimeTagEarning(requestingAccountId, tagId);
            BigDecimal currentMonthTagEarnings = this.transactionService.getCurrentMonthTagEarning(requestingAccountId, tagId);
            Map<String, BigDecimal> monthlyTagEarningsForLastYear = this.transactionService.getMonthlyTagEarningForLastYear(requestingAccountId, tagId);

            TagEarningsResp tagEarningsResp = new TagEarningsResp();
            tagEarningsResp.setLifetimeTagEarning(lifetimeTagEarnings);
            tagEarningsResp.setCurrentMonthTagEarning(currentMonthTagEarnings);
            tagEarningsResp.setMonthlyTagEarningForLastYear(monthlyTagEarningsForLastYear);

            return tagEarningsResp;
        }
        catch (AccountNotFoundException | TagNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
