package com.akshaychauhan.paymentgateway.payment.config;

import com.akshaychauhan.paymentgateway.common.enums.PaymentMethod;
import com.akshaychauhan.paymentgateway.payment.gateway.PaymentAdapter;
import com.akshaychauhan.paymentgateway.payment.gateway.adapter.CardPaymentAdapter;
import com.akshaychauhan.paymentgateway.payment.gateway.adapter.NetBankingAdapter;
import com.akshaychauhan.paymentgateway.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

 @Bean
 public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
    return Map.of(
            PaymentMethod.CARD, new CardPaymentAdapter(),
            PaymentMethod.NETBANKING,  new NetBankingAdapter(),
            PaymentMethod.UPI,  new UpiPaymentAdapter()
    );
  }
}
