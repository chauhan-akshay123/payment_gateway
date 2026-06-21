package com.akshaychauhan.paymentgateway.payment.repository;

import com.akshaychauhan.paymentgateway.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {


}
