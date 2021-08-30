package com.spring.kodo.service.impl;

import com.spring.kodo.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams.PaymentMethodOptions;
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
                        .setRefreshUrl("")
                        .setReturnUrl("")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();

        return AccountLink.create(params);
    }

    @Override
    public PaymentIntent createPaymentIntent(String connectedStripeAccountId, BigDecimal amount) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        ArrayList<String> paymentMethodTypes = new ArrayList<String>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", paymentMethodTypes);
        params.put("amount", amount);
        params.put("currency", "sgd");
        params.put("application_fee_amount", amount.multiply(KODO_PLATFORM_FEE_PERCENTAGE));

        Map<String, Object> transferDataParams = new HashMap<>();
        transferDataParams.put("destination", connectedStripeAccountId);
        params.put("transfer_data", transferDataParams);

        return PaymentIntent.create(params);
    }
}
