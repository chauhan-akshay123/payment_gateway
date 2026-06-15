package com.akshaychauhan.paymentgateway.merchant.repository;

import com.akshaychauhan.paymentgateway.merchant.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

}
