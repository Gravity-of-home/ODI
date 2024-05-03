package com.homegravity.Odi.global.redis.repository;

import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockRepository {

    private final RedissonClient redissonClient;

    public <T> T runOnRedissonLock(String key, Supplier<T> task) {
        if (key == null) {
            throw new BusinessException(ErrorCode.CONTENT_IS_NULL, ErrorCode.CONTENT_IS_NULL.getMessage());
        }
        RLock lock = redissonClient.getLock(key);
        try {
            //선행 락 점유 스레드가 존재하면 waitTime동안 락 점유를 기다리며 leaseTime 시간 이후로는 자동으로 락이 해제되기 때문에 다른 스레드도 일정 시간이 지난 후 락을 점유할 수 있습니다.
            if (!lock.tryLock(10, 10, TimeUnit.SECONDS)) {
                log.info("락 획득 실패");
                throw new BusinessException(ErrorCode.FAILED_TO_GET_LOCK, ErrorCode.FAILED_TO_GET_LOCK.getMessage());
            }
            log.info("락 획득 성공");
            return task.get();
        } catch (InterruptedException e) {
            log.info("catch문 실행");
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.FAILED_TO_GET_LOCK, ErrorCode.FAILED_TO_GET_LOCK.getMessage());
        } finally {
            log.info("finally문 실행");
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("언락 실행");
            }
        }
    }
}
