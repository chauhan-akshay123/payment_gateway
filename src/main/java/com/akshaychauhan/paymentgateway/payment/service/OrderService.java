package com.akshaychauhan.paymentgateway.payment.service;

import com.akshaychauhan.paymentgateway.payment.dto.request.CreateOrderRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.OrderResponse;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId , CreateOrderRequest request);
}
