package com.assignment.project.service;

import com.assignment.project.model.InteractionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.assignment.project.model.InteractionType.*;

@Service
@RequiredArgsConstructor
public class ViralityService {

    private final StringRedisTemplate redisTemplate;

    public void updateViralityScore(
            Long postId,
            InteractionType interactionType
    ) {

        String redisKey = "post:" + postId + ":virality_score";

        int points = switch (interactionType) {

            case BOT_REPLY -> 1;

            case HUMAN_LIKE -> 20;

            case HUMAN_COMMENT -> 50;
        };
        redisTemplate.opsForValue()
                .increment(redisKey, points);
    }

    public Long getViralityScore(Long postId) {

        String redisKey = "post:" + postId + ":virality_score";

        String value = redisTemplate.opsForValue().get(redisKey);

        return value == null ? 0 : Long.parseLong(value);
    }
}