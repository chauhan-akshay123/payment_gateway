package com.akshaychauhan.paymentgateway.payment.dto.response;

import com.akshaychauhan.paymentgateway.common.entity.Money;
import com.akshaychauhan.paymentgateway.common.enums.PaymentMethod;
import com.akshaychauhan.paymentgateway.common.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse (
      UUID id,
      UUID orderId,
      UUID merchantId,
      Money amount,
      PaymentStatus status,
      PaymentMethod method,
      Map<String, Object> methodDetails,
      String errorCode,
      String errorDescription,
      LocalDateTime capturedAt,
      LocalDateTime createdAt
) {
}
