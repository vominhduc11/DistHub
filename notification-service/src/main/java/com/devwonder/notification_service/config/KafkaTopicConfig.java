package com.devwonder.notification_service.config;

import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic emailNotificationTopic() {
        return TopicBuilder.name("email-notifications")
                .partitions(3)           // 3 partitions for better parallel processing
                .replicas(1)             // 1 replica
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, "delete")
                .config(TopicConfig.RETENTION_MS_CONFIG, "604800000") // 7 days retention
                .build();
    }
}