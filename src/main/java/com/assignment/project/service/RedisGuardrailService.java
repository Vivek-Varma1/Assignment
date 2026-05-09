package com.assignment.project.service;

import com.assignment.project.customExceptionHandler.RateLimitException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisGuardrailService {

    private final StringRedisTemplate redisTemplate;

    /*
        Horizontal Cap
        Max 100 bot replies per post
     */
    public void checkHorizontalCap(Long postId) {

        String key = "post:" + postId + ":bot_count";

        Long count = redisTemplate.opsForValue()
                .increment(key);

        if (count > 100) {

            // rollback increment
            redisTemplate.opsForValue().decrement(key);
            throw new RateLimitException(
                    "Bot limit exceeded for this post"
            );
        }
    }

    /*
        Vertical Cap
        Max thread depth = 20
     */
    public void checkVerticalCap(Integer depthLevel) {

        if (depthLevel > 20) {

            throw new RateLimitException(
                    "Thread depth exceeded"
            );
        }
    }

    /*
        Cooldown Cap
        Bot cannot interact with same human
        more than once in 10 mins
     */
    public void checkCooldownCap(
            Long botId,
            Long humanId
    ) {

        String key =
                "cooldown:bot_" + botId + ":human_" + humanId;

         /*
            Set key with TTL = 10 mins
         */

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(
                        key,
                        "BLOCKED",
                        10,
                        TimeUnit.MINUTES
                );

        if (Boolean.FALSE.equals(success)) {

            throw new RateLimitException(
                    "Cooldown active"
            );
        }



    }

    public void rollbackHorizontalCap(Long postId) {

        String key =
                "post:" + postId + ":bot_count";

        redisTemplate.opsForValue()
                .decrement(key);
    }
}