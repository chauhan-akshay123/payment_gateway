package com.akshaychauhan.paymentgateway.payment.simulator;

import com.akshaychauhan.paymentgateway.common.enums.ChaosMode;
import com.akshaychauhan.paymentgateway.common.enums.PaymentStatus;
import com.akshaychauhan.paymentgateway.common.util.RandomizerUtil;
import com.akshaychauhan.paymentgateway.payment.entity.Payment;
import com.akshaychauhan.paymentgateway.payment.repository.PaymentRepository;
import com.akshaychauhan.paymentgateway.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

  private final PaymentRepository paymentRepository;
  private final PaymentService paymentService;
  private final SimulatorConfig simulatorConfig;

  @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms:5000}")
  public void processCallbacks() {

      LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);

      List<Payment> candidates = paymentRepository
              .findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalWindow);

      if(candidates.isEmpty()) return;

      for(Payment payment : candidates) {
          simulateCallback(payment);
      }
  }

    private void simulateCallback(Payment payment) {
     SimulatorConfig.MethodSimulatorConfig methodConfig = simulatorConfig.configFor(payment.getMethod());

     LocalDateTime dueAt = dueAt(payment, methodConfig);

     if(LocalDateTime.now().isBefore(dueAt)) {
         return;
     }

     ChaosMode chaosMode = simulatorConfig.getChaosMode();

     switch (chaosMode) {
         case SUCCESS -> resolve(payment, true);
         case FAILURE -> resolve(payment, false);
         case TIMEOUT -> log.debug("BankCallback simulator: Payment Timed out");
         case NORMAL, SLOW -> resolve(payment, shoudlApprove(payment, methodConfig));
     }
    }

    private void resolve(Payment payment, boolean approve) {
        if (approve) {
            String bankRef = "SIM_BANK_REF"+ RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(), true, bankRef, null, null);
        } else {
            paymentService.resolveAuthorization(payment.getId(), false, null, "SIM_BANK_ERROR_CODE", "Simulated Bank Decline");
        }
    }

    private boolean shoudlApprove(Payment payment, SimulatorConfig.MethodSimulatorConfig methodConfig) {
      int bucket = Math.abs(payment.getId().hashCode()) % 100;
      return bucket < methodConfig.getSuccessRate();
    }

    private LocalDateTime dueAt(Payment payment, SimulatorConfig.MethodSimulatorConfig methodConfig) {

      int range = methodConfig.getMaxDelaySeconds() - methodConfig.getMinDelaySeconds();
      int delaySeconds = methodConfig.getMaxDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range+1);

      if(simulatorConfig.getChaosMode() == ChaosMode.SLOW) {
          delaySeconds += 2;
      }

      return payment.getCreatedAt().plusSeconds(delaySeconds);
    }
}
