package com.akshaychauhan.paymentgateway.merchant.dto.response;

import com.akshaychauhan.paymentgateway.common.enums.BusinessType;
import com.akshaychauhan.paymentgateway.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(

      UUID id,
      String name,
      String email,
      String businessName,
      BusinessType businessType,
      MerchantStatus merchantStatus
) {
}
