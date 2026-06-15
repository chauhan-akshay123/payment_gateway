package com.akshaychauhan.paymentgateway.merchant.service;

import com.akshaychauhan.paymentgateway.merchant.dto.request.CreateApiKeyRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.response.ApiKeyCreateResponse;
import com.akshaychauhan.paymentgateway.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
     ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);

     List<ApiKeyResponse> listByMerchant(UUID merchantId);
}
