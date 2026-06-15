package com.akshaychauhan.paymentgateway.merchant.service;

import com.akshaychauhan.paymentgateway.merchant.dto.request.MerchantSignupRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.response.MerchantResponse;
import org.jspecify.annotations.Nullable;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest request);
}
