package com.akshaychauhan.paymentgateway.payment.gateway;

import com.akshaychauhan.paymentgateway.payment.gateway.dto.PaymentRequest;
import com.akshaychauhan.paymentgateway.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);

    PaymentResult capture(UUID paymentId);
}
