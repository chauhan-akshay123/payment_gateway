package com.akshaychauhan.paymentgateway.common.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisIdempotencyStore implements IdempotencyStore{

    private static final String PREFIX = "idempotency:";
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean setIfAbsent(String key, Duration ttl) {
        try{
          Boolean set = stringRedisTemplate.opsForValue().setIfAbsent(PREFIX+key, IN_PROGRESS, ttl);
          return Boolean.TRUE.equals(set);
        } catch (DataAccessException e) {
            log.warn("Idempotency store unavailable. Key: {}, Error: {}", key, e.getMessage());
            return true;
        }
    }

    @Override
    public void store(String key, String value, Duration ttl) {
      try{
         stringRedisTemplate.opsForValue().set(PREFIX+key, value, ttl);
      } catch (Exception e) {
         log.warn("Failed to store persist, failing open for idempotency. Key: {}, Error: {}", key, e.getMessage());
      }
      }

    @Override
    public Optional<String> get(String key) {
        try{
          return Optional.ofNullable(stringRedisTemplate.opsForValue().get(PREFIX+key));
        } catch (Exception e) {
            log.warn("Failed to retrieve idempotency value. Key: {}, Error: {}", key, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void delete(String key) {
        try{
          stringRedisTemplate.delete(PREFIX+key);
        } catch (Exception e) {
               log.warn("Failed to delete idempotency value. Key: {}, Error: {}", key, e.getMessage());
        }
    }
}
