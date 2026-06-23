package com.akshaychauhan.paymentgateway.payment.controller;

import com.akshaychauhan.paymentgateway.payment.dto.request.PaymentInitRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.PaymentResponse;
import com.akshaychauhan.paymentgateway.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("09611975-e92c-468c-9774-37f16ab70727"); // TODO: replace it with merchantContext

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(
            @Valid @RequestBody PaymentInitRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId ,request));
    }
}
