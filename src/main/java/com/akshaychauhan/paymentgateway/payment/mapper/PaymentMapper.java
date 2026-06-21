package com.akshaychauhan.paymentgateway.payment.mapper;

import com.akshaychauhan.paymentgateway.payment.dto.response.PaymentResponse;
import com.akshaychauhan.paymentgateway.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResposneList(List<Payment> paymentList);
}
