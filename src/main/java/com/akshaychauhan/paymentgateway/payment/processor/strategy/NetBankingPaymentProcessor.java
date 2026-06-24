package com.akshaychauhan.paymentgateway.payment.processor.strategy;

import com.akshaychauhan.paymentgateway.payment.processor.PaymentProcessor;
import com.akshaychauhan.paymentgateway.payment.processor.dto.PaymentProcessorRequest;
import com.akshaychauhan.paymentgateway.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        // call the third party
        return null;
    }
}
