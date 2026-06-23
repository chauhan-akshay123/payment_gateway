package com.akshaychauhan.paymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaymentgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentgatewayApplication.class, args);
	}

}
