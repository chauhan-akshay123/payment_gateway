package com.akshaychauhan.paymentgateway.payment.controller;

import com.akshaychauhan.paymentgateway.payment.dto.request.PaymentInitRequest;
import com.akshaychauhan.paymentgateway.payment.dto.response.PaymentResponse;
import com.akshaychauhan.paymentgateway.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("ddb3a419-5624-4a31-bc5c-cda197e3b393"); // TODO: replace it with merchantContext

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(
            @Valid @RequestBody PaymentInitRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId ,request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(
            @PathVariable UUID paymentId
    ) {
        return ResponseEntity.ok(paymentService.capture(merchantId, paymentId));
    }
}
