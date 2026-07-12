package com.akshaychauhan.paymentgateway.vault.service;

import com.akshaychauhan.paymentgateway.common.entity.Money;
import com.akshaychauhan.paymentgateway.payment.processor.dto.PaymentProcessorResponse;
import com.akshaychauhan.paymentgateway.vault.dto.request.TokenizeRequest;
import com.akshaychauhan.paymentgateway.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

  TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

  PaymentProcessorResponse charge(UUID paymentId ,String token, Money amount, Map<String, Object> methodDetails);
}
