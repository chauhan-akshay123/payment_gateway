package com.akshaychauhan.paymentgateway.payment.controller;

import com.akshaychauhan.paymentgateway.payment.dto.request.CreateOrderRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.OrderResponse;
import com.akshaychauhan.paymentgateway.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    UUID merchantId = UUID.fromString("ddb3a419-5624-4a31-bc5c-cda197e3b393"); // TODO: replace it with MerchantContext

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(merchantId, request));
    }
}
