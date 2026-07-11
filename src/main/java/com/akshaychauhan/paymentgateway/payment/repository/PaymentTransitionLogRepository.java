package com.akshaychauhan.paymentgateway.payment.repository;

import com.akshaychauhan.paymentgateway.payment.entity.PaymentTransitionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTransitionLogRepository extends JpaRepository<PaymentTransitionLog, UUID> {
}
