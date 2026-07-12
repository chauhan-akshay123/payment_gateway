package com.akshaychauhan.paymentgateway.vault.dto.response;

import com.akshaychauhan.paymentgateway.common.enums.CardBrand;

public record TokenizeResponse (
      String token,
      String lastFour,
      CardBrand brand,
      Integer expiryMonth,
      Integer expiryYear
) {
}
