package com.akshaychauhan.paymentgateway.merchant.service.impl;

import com.akshaychauhan.paymentgateway.common.enums.MerchantStatus;
import com.akshaychauhan.paymentgateway.common.enums.UserRole;
import com.akshaychauhan.paymentgateway.common.exception.DuplicateResourceException;
import com.akshaychauhan.paymentgateway.common.exception.ResourceNotFoundException;
import com.akshaychauhan.paymentgateway.merchant.dto.request.LoginRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.request.MerchantSignupRequest;
import com.akshaychauhan.paymentgateway.merchant.dto.response.LoginResponse;
import com.akshaychauhan.paymentgateway.merchant.dto.response.MerchantResponse;
import com.akshaychauhan.paymentgateway.merchant.entity.AppUser;
import com.akshaychauhan.paymentgateway.merchant.entity.Merchant;
import com.akshaychauhan.paymentgateway.merchant.mapper.MerchantMapper;
import com.akshaychauhan.paymentgateway.merchant.repository.AppUserRepository;
import com.akshaychauhan.paymentgateway.merchant.repository.MerchantRepository;
import com.akshaychauhan.paymentgateway.merchant.security.JwtUtil;
import com.akshaychauhan.paymentgateway.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        log.info("Entering AuthServiceImpl.signup method for email={}", request.email());
        if(merchantRepository.existsByEmail(request.email())) {
            log.warn("Duplicate signup attempt detected for email={}", request.email());
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL"
                    ,"Merchant with email already exists: " + request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);

        merchant = merchantRepository.save(merchant);
        log.info("Merchant entity persisted with id={}, status={}", merchant.getId(), merchant.getStatus());

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);
        log.info("AppUser created for merchantId={}, role={}", merchant.getId(), appUser.getRole());

        return merchantMapper.toResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Entering AuthServiceImpl.login method for email={}", request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        String token = jwtUtil.generateAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().toString());

        log.info("Access token generated for userId={}, merchantId={}",
                appUser.getId(), appUser.getMerchant().getId());

        log.info("Login completed successfully for email={}", request.email());
        return new LoginResponse(token);
    }
}
