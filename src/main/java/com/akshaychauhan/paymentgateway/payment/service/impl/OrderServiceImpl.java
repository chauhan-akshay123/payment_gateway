package com.akshaychauhan.paymentgateway.payment.service.impl;

import com.akshaychauhan.paymentgateway.common.enums.EventAggregateType;
import com.akshaychauhan.paymentgateway.common.enums.OrderStatus;
import com.akshaychauhan.paymentgateway.common.exception.BusinessRuleViolationException;
import com.akshaychauhan.paymentgateway.common.exception.DuplicateResourceException;
import com.akshaychauhan.paymentgateway.common.exception.ResourceNotFoundException;
import com.akshaychauhan.paymentgateway.merchant.service.CustomerService;
import com.akshaychauhan.paymentgateway.payment.dto.request.CreateOrderRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.OrderResponse;
import com.akshaychauhan.paymentgateway.payment.dto.response.PaymentResponse;
import com.akshaychauhan.paymentgateway.payment.entity.OrderRecord;
import com.akshaychauhan.paymentgateway.payment.entity.Payment;
import com.akshaychauhan.paymentgateway.payment.mapper.OrderMapper;
import com.akshaychauhan.paymentgateway.payment.mapper.PaymentMapper;
import com.akshaychauhan.paymentgateway.payment.outbox.OutboxEventPublisher;
import com.akshaychauhan.paymentgateway.payment.repository.OrderRepository;
import com.akshaychauhan.paymentgateway.payment.repository.PaymentRepository;
import com.akshaychauhan.paymentgateway.payment.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;
    private final CustomerService customerService;
    private final OutboxEventPublisher eventPublisher;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;


    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        log.info("Entering OrderServiceImpl.create with merchantId: {}, request: {}", merchantId, request);

        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists: " + request.receipt());
        }

        UUID customerId = null;
        if(request.customer() != null){
            log.info("Customer details provided in request, finding or creating customer for merchantId: {}, email: {}", merchantId, request.customer().email());
           customerId = customerService.findOrCreate(merchantId,
                   request.customer().email(),
                   request.customer().name(),
                   request.customer().phone()
           );
        }

        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .customerId(customerId)
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);
        log.info("Order created with id: {}, merchantId: {}, receipt: {}", order.getId(), merchantId, request.receipt());

        // publish kafka event about order creation -> outboxEvent Table
        eventPublisher.publish(EventAggregateType.ORDER, order.getId(), "ORDER_CREATED",
                Map.of("orderId", order.getId(),
                       "merchantId", merchantId.toString(),
                        "status", order.getOrderStatus().name(),
                        "amount", order.getAmount(),
                        "ammountCurrency", order.getAmount().getCurrency()
                   )
                );

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(merchantId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL",
                    "Cannot cancel order with status: " + order.getOrderStatus().name());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        //publish kafka event about order cancellation -> outboxEvent table
        eventPublisher.publish(EventAggregateType.ORDER, order.getId(), "ORDER_CANCELLED",
                Map.of("orderId", order.getId(),
                       "merchantId", merchantId.toString(),
                       "status", order.getOrderStatus().name(),
                        "amountUnits", order.getAmount().getAmountUnits(),
                        "amountCurrency", order.getAmount().getCurrency()
                )
                );

        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        orderRepository.findByIdAndMerchantId(merchantId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        List<Payment> paymentList = paymentRepository.findByOrder_Id(orderId);

        return paymentMapper.toResposneList(paymentList);
    }
}
