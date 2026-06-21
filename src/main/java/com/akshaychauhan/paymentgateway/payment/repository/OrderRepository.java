package com.akshaychauhan.paymentgateway.payment.repository;

import com.akshaychauhan.paymentgateway.payment.dto.request.CreateOrderRequest;
import com.akshaychauhan.paymentgateway.payment.entity.OrderRecord;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {


    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);
}
