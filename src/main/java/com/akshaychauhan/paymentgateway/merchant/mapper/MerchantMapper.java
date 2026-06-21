package com.akshaychauhan.paymentgateway.merchant.mapper;

import com.akshaychauhan.paymentgateway.merchant.dto.request.MerchantSignupRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.response.MerchantResponse;
import com.akshaychauhan.paymentgateway.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignUpRequest(MerchantSignupRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
