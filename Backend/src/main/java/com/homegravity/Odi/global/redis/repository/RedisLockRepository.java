package com.homegravity.Odi.global.redis.repository;

import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private RedisTemplate<String, String> redisTemplate;

    public Boolean lock(String key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(String key) {
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(String key) {
        return key.toString();
    }

    public <T> T runOnLettuceLock(String key, Supplier<T> task) {
        while (true) {
            if (!lock(key)) {
                try {
                    log.info("key: {}의 락 획득 실패....T^T", key);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new BusinessException(ErrorCode.FAILED_TO_GET_LOCK, ErrorCode.FAILED_TO_GET_LOCK.getMessage());
                }
            } else {
                log.info("락 획득 성공, lock number: {}", key);
                break;
            }
        }

        try {
            return task.get();
        } finally {
            //Lock 해제
            log.info("락 해제");
            unlock(key);
        }
    }
}
