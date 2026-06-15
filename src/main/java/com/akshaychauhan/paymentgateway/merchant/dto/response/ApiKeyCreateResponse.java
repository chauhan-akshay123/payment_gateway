package com.akshaychauhan.paymentgateway.merchant.dto.response;

import com.akshaychauhan.paymentgateway.common.enums.Environment;
import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
