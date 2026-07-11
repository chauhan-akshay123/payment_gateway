package com.akshaychauhan.paymentgateway.payment.service;

import com.akshaychauhan.paymentgateway.payment.dto.request.PaymentInitRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.PaymentResponse;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);
}
