package com.spring.kodo.service.impl;

import com.spring.kodo.restentity.StripePaymentReq;
import com.spring.kodo.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams.PaymentMethodOptions;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeServiceImpl implements StripeService
{

    static final BigDecimal KODO_PLATFORM_FEE_PERCENTAGE = new BigDecimal(0.2);

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

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
                        .setRefreshUrl("http://localhost:8080")
                        .setReturnUrl("http://localhost:8080")
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
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setName(stripePaymentReq.getTutorName())
                                        .setAmount(stripePaymentReq.getAmount().longValue())
                                        .setCurrency("sgd")
                                        .setQuantity(1L)
                                        .build())
                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        .setApplicationFeeAmount(stripePaymentReq.getAmount().multiply(KODO_PLATFORM_FEE_PERCENTAGE).longValue())
                                        .setTransferData(
                                                SessionCreateParams.PaymentIntentData.TransferData.builder()
                                                        .setDestination(stripePaymentReq.getTutorStripeAccountId())
                                                        .build())
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://example.com/success")
                        .setCancelUrl("https://example.com/cancel")
                        .build();

        return Session.create(params).getUrl();
    }
}
