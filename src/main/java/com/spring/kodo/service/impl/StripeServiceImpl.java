package com.spring.kodo.service.impl;

import com.google.gson.JsonSyntaxException;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.restentity.request.StripePaymentReq;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.EnrolledCourseService;
import com.spring.kodo.service.inter.StripeService;
import com.spring.kodo.util.Constants;
import com.spring.kodo.util.exception.*;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class StripeServiceImpl implements StripeService
{
    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Value("${STRIPE_ENDPOINT_SECRET}")
    private String stripeEndpointSecret;

    @Value("${FE_MAIN_APP_URL}")
    private String mainAppUrl;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CourseService courseService;

    @Override
    public Account createNewStripeAccount() throws StripeException
    {
        Stripe.apiKey = stripeApiKey;

        AccountCreateParams params =
                AccountCreateParams
                        .builder()
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .build();

        return Account.create(params);
    }

    @Override
    public AccountLink createStripeAccountLink(Account account) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        AccountLinkCreateParams params =
                AccountLinkCreateParams
                        .builder()
                        .setAccount(account.getId())
                        .setRefreshUrl(mainAppUrl.concat("")) // TODO: Add refresh URL
                        .setReturnUrl(mainAppUrl.concat("profile")) // Redirect to profile page
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();

        return AccountLink.create(params);
    }

    @Override
    public String createStripeSession(StripePaymentReq stripePaymentReq) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .putMetadata("courseId", stripePaymentReq.getCourseId().toString())
                        .putMetadata("studentId", stripePaymentReq.getStudentId().toString())
                        .putMetadata("tutorId", stripePaymentReq.getTutorId().toString())
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setName(stripePaymentReq.getTutorName())
                                        .setAmount(stripePaymentReq.getAmount().longValue() * 100)
                                        .setCurrency("sgd")
                                        .setQuantity(1L)
                                        .build())
                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        .setApplicationFeeAmount(stripePaymentReq.getAmount().multiply(Constants.PLATFORM_FEE_PERCENTAGE).longValue() * 100)
                                        .setTransferData(
                                                SessionCreateParams.PaymentIntentData.TransferData.builder()
                                                        .setDestination(stripePaymentReq.getTutorStripeAccountId())
                                                        .build())
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(mainAppUrl.concat("browsecourse/preview/").concat(stripePaymentReq.getCourseId().toString())) // TODO: Add success url
                        .setCancelUrl(mainAppUrl) // TODO: Add cancel url
                        .build();

        return Session.create(params).getUrl();
    }

    @Override
    public void handleSuccessfulStripeCheckout(String payload, HttpServletRequest request) throws SignatureVerificationException, JsonSyntaxException, AccountNotFoundException, UnknownPersistenceException, InputDataValidationException, UpdateAccountException, CreateNewEnrolledCourseException, EnrolledCourseNotFoundException, CourseNotFoundException {
        Stripe.apiKey = stripeApiKey;
        String header = request.getHeader("Stripe-Signature");
        String endpointSecret = stripeEndpointSecret;

        Event event = null;

        event = Webhook.constructEvent(payload, header, endpointSecret);

        if (event.getType().equals("checkout.session.completed"))
        {
            Session session = (Session) event.getData().getObject();
            handleCompletedCheckoutSession(session);
        }
    }

    @Override
    public void handleCompletedCheckoutSession(Session session) throws AccountNotFoundException, CourseNotFoundException, UnknownPersistenceException, CreateNewEnrolledCourseException, InputDataValidationException, EnrolledCourseNotFoundException, UpdateAccountException {
        // Update Transaction entity
        System.out.println(session);

        Map<String, String> stripeSessionMetadata = session.getMetadata();
        Long courseId = Long.parseLong(stripeSessionMetadata.getOrDefault("courseId", ""));
        Long studentId = Long.parseLong(stripeSessionMetadata.getOrDefault("studentId", ""));
        Long tutorId = Long.parseLong(stripeSessionMetadata.getOrDefault("tutorId", ""));

        com.spring.kodo.entity.Account student = accountService.getAccountByAccountId(studentId);
        Course course = courseService.getCourseByCourseId(courseId);

        EnrolledCourse enrolledCourse = enrolledCourseService.createNewEnrolledCourse(student.getAccountId(), course.getCourseId());
        accountService.addEnrolledCourseToAccount(student, enrolledCourse);
    }
}