package com.spring.kodo.service.impl;

import com.google.gson.Gson;
import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.entity.Transaction;
import com.spring.kodo.repository.TransactionRepository;
import com.spring.kodo.entity.rest.response.CourseWithEarningResp;
import com.spring.kodo.entity.rest.response.MonthlyEarningResp;
import com.spring.kodo.entity.rest.response.TransactionWithParticularsResp;
import com.spring.kodo.entity.rest.response.TutorWithEarningResp;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.TagService;
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

    @Autowired
    private TagService tagService;

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

    // takes in tutor's account id
    @Override
    public List<Transaction> getAllEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        return this.transactionRepository.getAllTransactionByPayeeId(requestingAccount.getAccountId());
    }


    @Override
    public BigDecimal getLifetimeTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        return this.transactionRepository.getLifetimeEarningsByPayeeId(requestingAccount.getAccountId()).orElse(new BigDecimal(0));
    }

    @Override
    public BigDecimal getLifetimeTutorPayoutByCourseId(Long courseId)
    {
        return this.transactionRepository.getLifetimeTutorPayoutByCourseId(courseId).orElse(new BigDecimal(0));
    }

    @Override
    public List<Map<String, String>> getLifetimeEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException {

        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        List<Course> myCourses = requestingAccount.getCourses();

        List<Map<String, String>> outputList = new ArrayList<>();
        for (Course c: myCourses)
        {
            BigDecimal lifetimeEarningFromCourse = getLifetimeTutorPayoutByCourseId(c.getCourseId());
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
    public BigDecimal getAverageMonthlyCourseEarning(Long courseId)
    {
        return this.transactionRepository.getAverageMonthlyEarningByCourseId(courseId).orElse(new BigDecimal(0));
    }

    @Override
    public String getHighestEarningMonthWithValueByCourseId(Long courseId)
    {
        Map<String, Object> result = this.transactionRepository.getHighestEarningMonthWithValue(courseId);
        if (result.get("MonthSum") != null)
        {
            String monthShortName = NowMonthYearUtil.getMonthListShortName().get((int) result.get("DisMonth") - 1);
            // E.g. Aug 2021 ($123.00)
            return monthShortName + " " + result.get("DisYear") + " ($" + result.get("MonthSum") + ")";
        }
        else
        {
            return "";
        }
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
    public List<Map<String, String>> getCourseStatsByMonthForLastYear(Long requestingAccountId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException {

        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        List<Course> myCourses = requestingAccount.getCourses();
        List<Map<String, String>> outputList = new ArrayList<>();

        for (Course c: myCourses)
        {
            Map<String,String> inputData = new HashMap<>();
            inputData.put("courseId", c.getCourseId().toString());
            inputData.put("courseName", c.getName());
            inputData.put("lifetimeEarnings",  getLifetimeTutorPayoutByCourseId(c.getCourseId()).toString());
            inputData.put("currentMonthEarnings", this.transactionRepository.getTutorPayoutByMonthByCourseId(c.getCourseId(), NowMonthYearUtil.getNowYear().getValue(), NowMonthYearUtil.getNowMonth()).orElse(new BigDecimal(0)).toString());
            inputData.put("monthlyAverageEarnings", getAverageMonthlyCourseEarning(c.getCourseId()).toString());
            inputData.put("highestEarningMonthWithValue", getHighestEarningMonthWithValueByCourseId(c.getCourseId()));

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
            return this.transactionRepository.getLifetimePlatformEarning().orElse(new BigDecimal(0));
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
            return this.transactionRepository.getCurrentMonthPlatformEarning().orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getLastMonthPlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLastMonthPlatformEarning().orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<MonthlyEarningResp> getMonthlyPlatformEarningsForLastYear(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            LocalDate today = LocalDate.now();
            LocalDate currentMonth = today.withDayOfMonth(1);

            List<MonthlyEarningResp> monthlyPlatformEarningsForLastYear = new ArrayList<>();

            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();

            for (int i = 0; i < 12; i++)
            {
                int month = currentMonth.getMonth().getValue();
                int year = currentMonth.getYear();

                BigDecimal earningsForCurrentMonth = this.transactionRepository.getMonthlyPlatformEarning(year, month).orElse(new BigDecimal(0));

                monthlyPlatformEarningsForLastYear.add(new MonthlyEarningResp(monthListShortName.get(month-1) + " " + year, earningsForCurrentMonth));

                currentMonth = currentMonth.minusMonths(1);
            }

            // Reverse value to return earnings in sequential ordering
            Collections.reverse(monthlyPlatformEarningsForLastYear);

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
            return this.transactionRepository.getLifetimeCourseEarning(course.getCourseId()).orElse(new BigDecimal(0));
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

        if (requestingAccount.getIsAdmin() || requestingAccount.getCourses().contains(course))
        {
            return this.transactionRepository.getCurrentMonthCourseEarning(course.getCourseId()).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin or course owner");
        }
    }

    @Override
    public List<MonthlyEarningResp> getMonthlyCourseEarningForLastYear(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            LocalDate today = LocalDate.now();
            LocalDate currentMonth = today.withDayOfMonth(1);

            List<MonthlyEarningResp> monthlyCourseEarningsForLastYear = new ArrayList<>();

            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();

            for (int i = 0; i < 12; i++)
            {
                int month = currentMonth.getMonth().getValue();
                int year = currentMonth.getYear();

                BigDecimal earningsForCurrentMonth = this.transactionRepository.getMonthlyCourseEarningForLastYear(course.getCourseId(), year, month).orElse(new BigDecimal(0));

                monthlyCourseEarningsForLastYear.add(new MonthlyEarningResp(monthListShortName.get(month-1) + " " + year, earningsForCurrentMonth));

                currentMonth = currentMonth.minusMonths(1);
            }

            // Reverse value to return earnings in sequential ordering
            Collections.reverse(monthlyCourseEarningsForLastYear);

            return monthlyCourseEarningsForLastYear;
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getLifetimeTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Tag tag = this.tagService.getTagByTagId(tagId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLifetimeTagEarning(tag.getTagId()).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getCurrentMonthTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Tag tag = this.tagService.getTagByTagId(tagId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthTagEarning(tag.getTagId()).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getLastMonthTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Tag tag = this.tagService.getTagByTagId(tagId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLastMonthTagEarning(tag.getTagId()).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<MonthlyEarningResp> getMonthlyTagEarningForLastYear(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Tag tag = this.tagService.getTagByTagId(tagId);

        if (requestingAccount.getIsAdmin())
        {
            LocalDate today = LocalDate.now();
            LocalDate currentMonth = today.withDayOfMonth(1);

            List<MonthlyEarningResp> monthlyTagEarningsForLastYear = new ArrayList<>();

            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();

            for (int i = 0; i < 12; i++)
            {
                int month = currentMonth.getMonth().getValue();
                int year = currentMonth.getYear();

                BigDecimal earningsForCurrentMonth = this.transactionRepository.getMonthlyTagEarningForLastYear(tagId, year, month).orElse(new BigDecimal(0));

                monthlyTagEarningsForLastYear.add(new MonthlyEarningResp(monthListShortName.get(month-1) + " " + year, earningsForCurrentMonth));

                currentMonth = currentMonth.minusMonths(1);
            }

            // Reverse value to return earnings in sequential ordering
            Collections.reverse(monthlyTagEarningsForLastYear);

            return monthlyTagEarningsForLastYear;
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getLifetimeTutorEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Account tutorAccount = this.accountService.getAccountByAccountId(tutorId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLifetimeEarningsByPayeeId(tutorAccount.getAccountId()).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getCurrentMonthTutorEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Account tutorAccount = this.accountService.getAccountByAccountId(tutorId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthEarningsByPayeeId(tutorAccount.getAccountId()).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<MonthlyEarningResp> getMonthlyTutorEarningForLastYear(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Account tutorAccount = this.accountService.getAccountByAccountId(tutorId);

        if (requestingAccount.getIsAdmin())
        {
            LocalDate today = LocalDate.now();
            LocalDate currentMonth = today.withDayOfMonth(1);

            List<MonthlyEarningResp> monthlyTutorEarningsForLastYear = new ArrayList<>();

            List<String> monthListShortName = NowMonthYearUtil.getMonthListShortName();

            for (int i = 0; i < 12; i++)
            {
                int month = currentMonth.getMonth().getValue();
                int year = currentMonth.getYear();

                BigDecimal earningsForCurrentMonth = this.transactionRepository.getMonthlyTransactionByPayeeId(tutorAccount.getAccountId(), year, month).orElse(new BigDecimal(0));

                monthlyTutorEarningsForLastYear.add(new MonthlyEarningResp(monthListShortName.get(month-1) + " " + year, earningsForCurrentMonth));

                currentMonth = currentMonth.minusMonths(1);
            }

            // Reverse value to return earnings in sequential ordering
            Collections.reverse(monthlyTutorEarningsForLastYear);

            return monthlyTutorEarningsForLastYear;
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<CourseWithEarningResp> getLifetimeHighestEarningCourses(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLifetimeHighestEarningCourses();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getLastMonthCourseEarning(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLastMonthCourseEarning(courseId).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<CourseWithEarningResp> getCurrentMonthHighestEarningCourses(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthHighestEarningCourses();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getNumEnrolledCurrentMonthByCourseId(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException , CourseNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getNumEnrolledCurrentMonthByCourseId(courseId).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<TutorWithEarningResp> getLifetimeHighestEarningTutors(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getLifetimeHighestEarningTutors();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getNumEnrolledLastMonthByCourseId(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException , CourseNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getNumEnrolledLastMonthByCourseId(courseId).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public List<TutorWithEarningResp> getCurrentMonthHighestEarningTutors(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthHighestEarningTutors();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not a admin");
        }
    }

    @Override
    public BigDecimal getNumStudentsCompleted(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException , CourseNotFoundException
    {

        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Course course = this.courseService.getCourseByCourseId(courseId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getNumStudentsCompleted(courseId).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not an admin");
        }

    }

    @Override
    public BigDecimal getLastMonthTutorEarnings(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);
        Account tutor = this.accountService.getAccountByAccountId(tutorId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.lastMonthTutorEarnings(tutorId).orElse(new BigDecimal(0));
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not an admin");
        }
    }

    @Override
    public Integer getCurrentMonthEnrollmentCount(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getCurrentMonthEnrollmentCount().orElse(0);
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not an admin");
        }
    }

    @Override
    public Integer getPreviousMonthEnrollmentCount(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getPreviousMonthEnrollmentCount().orElse(0);
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not an admin");
        }
    }

    @Override
    public List<TransactionWithParticularsResp> getTransactionsWithParticularsForLastThirtyDays(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = this.accountService.getAccountByAccountId(requestingAccountId);

        if (requestingAccount.getIsAdmin())
        {
            return this.transactionRepository.getTransactionsWithParticularsForLastThirtyDays();
        }
        else
        {
            throw new AccountPermissionDeniedException("Account is not an admin");
        }
    }
}
