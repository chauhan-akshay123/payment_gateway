package com.akshaychauhan.paymentgateway.payment.mapper;

import com.akshaychauhan.paymentgateway.payment.dto.response.OrderResponse;
import com.akshaychauhan.paymentgateway.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

   OrderResponse toResponse(OrderRecord orderRecord);
}
