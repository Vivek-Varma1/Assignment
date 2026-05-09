package com.assignment.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StringRedisTemplate redisTemplate;

    /*
        Send or Batch Notification
     */
    public void handleBotInteractionNotification(
            Long userId,
            String notificationMessage
    ) {

        String cooldownKey =
                "user:" + userId + ":notif_cooldown";

        String pendingNotifKey =
                "user:" + userId + ":pending_notifs";

        /*
            Check if cooldown exists
         */
        Boolean cooldownExists =
                redisTemplate.hasKey(cooldownKey);

        /*
            If user recently received notification
            -> batch notifications
         */
        if (Boolean.TRUE.equals(cooldownExists)) {

            redisTemplate.opsForList()
                    .rightPush(
                            pendingNotifKey,
                            notificationMessage
                    );

            System.out.println(
                    "Notification batched in Redis list"
            );

            return;
        }

        /*
            Send immediate notification
         */
        System.out.println(
                "Push Notification Sent to User : "
                        + userId
        );

        /*
            Set 15-minute cooldown
         */
        redisTemplate.opsForValue().set(
                cooldownKey,
                "ACTIVE",
                15,
                TimeUnit.MINUTES
        );
    }
}