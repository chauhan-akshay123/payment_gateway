package com.akshaychauhan.paymentgateway.payment.gateway.dto;

import com.akshaychauhan.paymentgateway.common.entity.Money;
import com.akshaychauhan.paymentgateway.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
