package com.akshaychauhan.paymentgateway.payment.gateway;

import com.akshaychauhan.paymentgateway.common.enums.PaymentMethod;
import com.akshaychauhan.paymentgateway.payment.gateway.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

    public void initiate(PaymentRequest request) {
        PaymentAdapter adapter = paymentAdapters.get(request.method());
        if(adapter == null) {
            throw new IllegalArgumentException("No payment adapter registered for method: "+request.method());
        }
        adapter.initiate(request);
    }
}
