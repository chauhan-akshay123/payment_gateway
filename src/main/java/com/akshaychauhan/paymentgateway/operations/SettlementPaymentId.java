package com.akshaychauhan.paymentgateway.operations;

import com.akshaychauhan.paymentgateway.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SettlementPaymentId {

    @Column(name = "settlement_id")
    private UUID settlementId;

    @Column(name = "payment_id")
    private UUID paymentId;

    public SettlementPaymentId() {
    }

    public SettlementPaymentId(UUID settlementId, UUID paymentId) {
        this.settlementId = settlementId;
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettlementPaymentId that)) return false;
        return Objects.equals(settlementId, that.settlementId)
                && Objects.equals(paymentId, that.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settlementId, paymentId);
    }
}
