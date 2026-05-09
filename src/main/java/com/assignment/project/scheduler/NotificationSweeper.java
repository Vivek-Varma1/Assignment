package com.assignment.project.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationSweeper {

    private final StringRedisTemplate redisTemplate;

    /*
        Runs every 5 mins
        (Testing version)
     */
    @Scheduled(fixedRate = 300000)
    public void sweepNotifications() {

        System.out.println(
                "Running Notification Sweeper..."
        );

        /*
            Find all pending notification lists
         */
        Set<String> keys =
                redisTemplate.keys(
                        "user:*:pending_notifs"
                );

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            List<String> notifications =
                    redisTemplate.opsForList()
                            .range(key, 0, -1);

            if (notifications == null
                    || notifications.isEmpty()) {

                continue;
            }

            /*
                Extract userId
             */
            String[] parts = key.split(":");

            String userId = parts[1];

            /*
                Example:
                Bot X and 4 others interacted
             */
            String firstNotification =
                    notifications.get(0);

            int remainingCount =
                    notifications.size() - 1;

            String summaryMessage;

            if (remainingCount > 0) {

                summaryMessage =
                        firstNotification
                                + " and "
                                + remainingCount
                                + " others interacted with your posts.";

            } else {

                summaryMessage =
                        firstNotification;
            }

            /*
                Simulate push notification
             */
            System.out.println(
                    "Summarized Push Notification for User "
                            + userId
                            + " : "
                            + summaryMessage
            );

            /*
                Clear Redis List
             */
            redisTemplate.delete(key);
        }
    }
}