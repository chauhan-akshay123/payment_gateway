package com.akshaychauhan.paymentgateway.operations;

import com.akshaychauhan.paymentgateway.common.entity.BaseEntity;
import com.akshaychauhan.paymentgateway.payment.entity.Payment;
import jakarta.persistence.*;

@Entity
@Table(name = "settlement_payment")
public class SettlementPayment extends BaseEntity {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

    @MapsId("paymentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
}
