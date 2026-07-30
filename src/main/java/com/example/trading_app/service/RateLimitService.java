package com.example.trading_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Configuration constants
    private static final String RATE_LIMIT_PREFIX = "rate_limit:signin:";
    private static final int MAX_ATTEMPTS = 5;  // Maximum attempts
    private static final long WINDOW_SIZE_MINUTES = 15;  // Time window in minutes

    /**
     * Check if a user/IP has exceeded the rate limit
     * @param identifier - email or IP address
     * @return true if within limit, false if limit exceeded
     */
    public boolean isAllowed(String identifier) {
        String key = RATE_LIMIT_PREFIX + identifier;

        // Get current attempt count
        Object attempts = redisTemplate.opsForValue().get(key);

        if (attempts == null) {
            // First attempt
            redisTemplate.opsForValue().set(key, 1, WINDOW_SIZE_MINUTES, TimeUnit.MINUTES);
            return true;
        }

        int currentAttempts = Integer.parseInt(attempts.toString());

        // Check if exceeded max attempts
        if (currentAttempts >= MAX_ATTEMPTS) {
            return false;
        }

        // Increment attempts
        redisTemplate.opsForValue().increment(key);
        return true;
    }

    /**
     * Get remaining attempts for a user/IP
     * @param identifier - email or IP address
     * @return remaining attempts
     */
    public int getRemainingAttempts(String identifier) {
        String key = RATE_LIMIT_PREFIX + identifier;
        Object attempts = redisTemplate.opsForValue().get(key);

        if (attempts == null) {
            return MAX_ATTEMPTS;
        }

        int currentAttempts = Integer.parseInt(attempts.toString());
        return Math.max(0, MAX_ATTEMPTS - currentAttempts);
    }

    /**
     * Get time remaining for rate limit window
     * @param identifier - email or IP address
     * @return time remaining in seconds
     */
    public long getTimeRemainingInSeconds(String identifier) {
        String key = RATE_LIMIT_PREFIX + identifier;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null && ttl > 0 ? ttl : 0;
    }

    /**
     * Reset rate limit for a user/IP (optional - for manual reset)
     * @param identifier - email or IP address
     */
    public void resetRateLimit(String identifier) {
        String key = RATE_LIMIT_PREFIX + identifier;
        redisTemplate.delete(key);
    }
}

