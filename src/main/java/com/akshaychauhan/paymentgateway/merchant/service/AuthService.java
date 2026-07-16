package com.akshaychauhan.paymentgateway.merchant.service;

import com.akshaychauhan.paymentgateway.merchant.dto.request.LoginRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.request.MerchantSignupRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.response.LoginResponse;
import com.akshaychauhan.paymentgateway.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(@Valid LoginRequest request);
}
