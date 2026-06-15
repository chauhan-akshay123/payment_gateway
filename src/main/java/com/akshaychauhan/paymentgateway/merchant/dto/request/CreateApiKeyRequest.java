package com.akshaychauhan.paymentgateway.merchant.dto.request;

import com.akshaychauhan.paymentgateway.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
