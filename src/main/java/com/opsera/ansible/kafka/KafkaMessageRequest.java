package com.opsera.ansible.kafka;

import lombok.Data;

@Data
public class KafkaMessageRequest {
    private KafkaTopics kafkaTopicName;
    private String message;
}
