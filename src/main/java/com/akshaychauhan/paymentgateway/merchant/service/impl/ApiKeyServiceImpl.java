package com.akshaychauhan.paymentgateway.merchant.service.impl;

import com.akshaychauhan.paymentgateway.common.exception.ResourceNotFoundException;
import com.akshaychauhan.paymentgateway.common.util.RandomizerUtil;
import com.akshaychauhan.paymentgateway.merchant.dto.request.CreateApiKeyRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.response.ApiKeyCreateResponse;
import com.akshaychauhan.paymentgateway.merchant.dto.response.ApiKeyResponse;
import com.akshaychauhan.paymentgateway.merchant.entity.ApiKey;
import com.akshaychauhan.paymentgateway.merchant.entity.Merchant;
import com.akshaychauhan.paymentgateway.merchant.repository.ApiKeyRepository;
import com.akshaychauhan.paymentgateway.merchant.repository.MerchantRepository;
import com.akshaychauhan.paymentgateway.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_"+request.environment().name().toLowerCase()+"_"+RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: encode with bcrypt encoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(
                apiKey.getId(),
                keyId,
                rawSecret,
                request.environment()
        );
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyRepository.findByMerchant_Id(merchantId).stream()
                .map(apiKey -> new ApiKeyResponse(
                    apiKey.getId(),
                    apiKey.getKeyId(),
                    apiKey.getEnvironment(),
                    apiKey.isEnabled(),
                    apiKey.getLastUsedAt(),
                    null
                ))
                .toList();
    }
}
