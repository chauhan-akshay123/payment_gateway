package com.akshaychauhan.paymentgateway.payment.service.impl;

import com.akshaychauhan.paymentgateway.common.enums.OrderStatus;
import com.akshaychauhan.paymentgateway.common.exception.DuplicateResourceException;
import com.akshaychauhan.paymentgateway.payment.dto.request.CreateOrderRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.OrderResponse;
import com.akshaychauhan.paymentgateway.payment.entity.OrderRecord;
import com.akshaychauhan.paymentgateway.payment.repository.OrderRepository;
import com.akshaychauhan.paymentgateway.payment.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists: " + request.receipt());
        }

        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);

// TODO: publis kafka event about order creation

        return new OrderResponse(order.getId(),
                order.getMerchantId(),
                order.getReceipt(),
                order.getAmount(),
                order.getOrderStatus(),
                order.getAttempts(),
                order.getNotes(),
                order.getExpiresAt(),
                null);
    }
}
