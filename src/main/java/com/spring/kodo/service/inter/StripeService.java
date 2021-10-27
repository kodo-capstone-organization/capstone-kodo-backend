package com.spring.kodo.service.inter;

import com.google.gson.JsonSyntaxException;
import com.spring.kodo.entity.rest.request.StripePaymentReq;
import com.spring.kodo.util.exception.*;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;

import javax.servlet.http.HttpServletRequest;

public interface StripeService
{
    Account createNewStripeAccount(Long accountId) throws StripeException;

    AccountLink createStripeAccountLink(Account account) throws StripeException;

    String createStripeSession(StripePaymentReq stripePaymentReq) throws StripeException;

    void handleIncomingStripeWebhook(String payload, HttpServletRequest request) throws SignatureVerificationException, JsonSyntaxException, AccountNotFoundException, UnknownPersistenceException, InputDataValidationException, UpdateAccountException, CreateNewEnrolledCourseException, EnrolledCourseNotFoundException, CourseNotFoundException, StudentAttemptNotFoundException, TagNotFoundException, TagNameExistsException, AccountEmailExistException, TransactionStripeTransactionIdExistsException, EnrolledLessonNotFoundException, UpdateEnrolledCourseException, ContentNotFoundException, LessonNotFoundException, UpdateEnrolledLessonException, CreateNewEnrolledLessonException, EnrolledContentNotFoundException, CreateNewEnrolledContentException;
}
