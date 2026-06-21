package com.akshaychauhan.paymentgateway.payment.dto.response;

import com.akshaychauhan.paymentgateway.common.entity.Money;
import com.akshaychauhan.paymentgateway.common.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse (
    UUID id,
    UUID merchantId,
    String receipt,
    Money amount,
    OrderStatus status,
    Integer attempts,
    Map<String, Object> notes,
    LocalDateTime expiresAt,
    LocalDateTime createdAt
)
{}

