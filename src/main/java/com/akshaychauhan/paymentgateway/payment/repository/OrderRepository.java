package com.akshaychauhan.paymentgateway.payment.repository;

import com.akshaychauhan.paymentgateway.payment.dto.response.OrderResponse;
import com.akshaychauhan.paymentgateway.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {


    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);
}
