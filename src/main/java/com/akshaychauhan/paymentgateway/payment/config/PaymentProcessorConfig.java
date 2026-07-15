package com.akshaychauhan.paymentgateway.payment.config;

import com.akshaychauhan.paymentgateway.common.enums.PaymentMethod;
import com.akshaychauhan.paymentgateway.payment.processor.PaymentProcessor;
import com.akshaychauhan.paymentgateway.payment.processor.strategy.CardPaymentProcessor;
import com.akshaychauhan.paymentgateway.payment.processor.strategy.NetBankingPaymentProcessor;
import com.akshaychauhan.paymentgateway.payment.processor.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor
        );
    }
}
