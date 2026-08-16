package com.akshaychauhan.paymentgateway.merchant.service.impl;

import com.akshaychauhan.paymentgateway.common.exception.ResourceNotFoundException;
import com.akshaychauhan.paymentgateway.merchant.entity.Customer;
import com.akshaychauhan.paymentgateway.merchant.entity.Merchant;
import com.akshaychauhan.paymentgateway.merchant.repository.CustomerRepository;
import com.akshaychauhan.paymentgateway.merchant.repository.MerchantRepository;
import com.akshaychauhan.paymentgateway.merchant.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final MerchantRepository merchantRepository;

    private final CustomerRepository customerRepository;

    @Override
    public UUID findOrCreate(UUID merchantId, String email, String name, String phone) {
        log.info("Entering CustomerServiceImpl.findOrCreate with merchantId: {}, email: {}, name: {}, phone: {}", merchantId, email, name, phone);

        if(email == null || email.isBlank()){
            return null;
        }

        return customerRepository.findByMerchant_IdAndEmail(merchantId, email)
                .map(Customer::getId)
                .orElseGet(() -> createNew(merchantId, email, name, phone));
    }

    private UUID createNew(UUID merchantId, String email, String name, String phone) {
        log.info("Etering CustomerServiceImpl.createNew with merchantId: {}, email: {}, name: {}, phone: {}", merchantId, email, name, phone);
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        Customer customer = Customer.builder()
                .merchant(merchant)
                .email(email)
                .name(name)
                .phone(phone)
                .build();

        customerRepository.save(customer);
        log.info("Customer created via findOrCreate id: {}, merchantId: {}, email: {}, name: {}, phone: {}", customer.getId(), merchantId, email, name, phone);
        return customer.getId();
    }
}
