package com.akshaychauhan.paymentgateway.merchant.mapper;

import com.akshaychauhan.paymentgateway.merchant.dto.response.ApiKeyCreateResponse;
import com.akshaychauhan.paymentgateway.merchant.dto.response.ApiKeyResponse;
import com.akshaychauhan.paymentgateway.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

 ApiKeyCreateResponse toCreateResponse(ApiKey apiKey);

 List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
