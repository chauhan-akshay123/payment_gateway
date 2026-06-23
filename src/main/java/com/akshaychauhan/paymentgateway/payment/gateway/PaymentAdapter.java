package com.akshaychauhan.paymentgateway.payment.gateway;

import com.akshaychauhan.paymentgateway.payment.gateway.dto.PaymentRequest;

public interface PaymentAdapter {

    void initiate(PaymentRequest request);
}
