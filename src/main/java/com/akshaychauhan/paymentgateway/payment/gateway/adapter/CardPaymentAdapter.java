package com.akshaychauhan.paymentgateway.payment.gateway.adapter;

import com.akshaychauhan.paymentgateway.payment.gateway.PaymentAdapter;
import com.akshaychauhan.paymentgateway.payment.gateway.dto.PaymentRequest;
import com.akshaychauhan.paymentgateway.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    public PaymentResult initiate(PaymentRequest request) {
       return null;
    }
}
