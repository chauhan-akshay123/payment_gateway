package com.akshaychauhan.paymentgateway.common.idempotency;

import com.akshaychauhan.paymentgateway.common.exception.IdempotencyConflictException;
import com.akshaychauhan.paymentgateway.merchant.security.MerchantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final Set<String> GUARDED_METHODS = Set.of("POST", "PUT", "PATCH");
    private static final Duration IN_PROGRESS_TTL = Duration.ofSeconds(30);
    private static final Duration COMPLETED_TTL = Duration.ofHours(24);
    private static final String SEPERATOR = "|";

    private final MerchantContext merchantContext;
    private final IdempotencyStore idempotencyStore;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!GUARDED_METHODS.contains(request.getMethod())) {
           filterChain.doFilter(request, response);
           return;
        }

        String rawKey = resolveIdempotencyKey(request);
        if(rawKey == null || rawKey.isBlank()) {
           filterChain.doFilter(request, response);
           return;
        }

        UUID merchantId = merchantContext.getMerchantId();
        String key = merchantId != null ? merchantId+":" + rawKey : rawKey;

        boolean claimed = idempotencyStore.setIfAbsent(key, IN_PROGRESS_TTL);

        if(!claimed) {
            // another thread has already claimed this key
            Optional<String> existing = idempotencyStore.get(key);

            if(existing.isPresent() && !IdempotencyStore.IN_PROGRESS.equals(existing.get())) {
              // It's not in progress, but coming from the actual value stored in redis
              replay(request, response , existing.get());
            } else {
                // It's in progress by another thread, we can either wait or fail fast. For simplicity, let's fail fast here.
                var ex = new IdempotencyConflictException("A request with this Idempotency-key is in progress. Please wait and try again later.");
                handlerExceptionResolver.resolveException(request, response, null, ex);
            }
            return;
        }

        // first time claim
        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        try{
          filterChain.doFilter(request, wrapper);
        } finally {
          int status = wrapper.getStatus();
          byte[] bodyBytes = wrapper.getContentAsByteArray();
          String body = new String(bodyBytes, StandardCharsets.UTF_8);

          if(status < 400 && bodyBytes.length > 0) {
            // Success - store the completed response for future replays
            String stored = status + SEPERATOR + body;
            idempotencyStore.store(key, stored, COMPLETED_TTL);
            log.debug("IdempotencyFilter: stored response status={} key={}", status, key);
          } else {
              // Error or empty - delete placeholder so client can retry cleanly
              idempotencyStore.delete(key);
              log.debug("IdempotencyFilter: deleted placeholder after error status={} key={}", status, key);
          }
          // Always flush buffered body to the actual response, otherwise the client will never get a response.
          // If this is skipped client receives an empty body and the request appears to hang until timeout.
          wrapper.copyBodyToResponse();
        }
     }

     private String resolveIdempotencyKey(HttpServletRequest request) {
        String key = request.getHeader("X-Idempotency-Key");
        if(key == null || key.isBlank()) {
            key = request.getHeader("Idempotency-Key");
        }
        return key;
     }

     private void replay(HttpServletRequest request, HttpServletResponse response, String stored) throws IOException {
        int seperatorIndex = stored.indexOf(SEPERATOR);
        if(seperatorIndex < 0){
            var ex = new IdempotencyConflictException("A request with this Idempotency-key is in progress. Please wait and try again later.");
            handlerExceptionResolver.resolveException(request, response, null, ex);
            return;
        }

        int status = Integer.parseInt(stored.substring(0, seperatorIndex));
        String body = stored.substring(seperatorIndex + 1);

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
     }
}
