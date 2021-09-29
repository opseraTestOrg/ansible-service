package com.opsera.ansible.dto;

import lombok.Getter;

/**
 * 
 * @author sreeni
 *This class is used to maintain the kafka topics required for the ansible integration
 */
@Getter
public enum KafkaTopics {

    ANSIBLE_RESPONSE_TOPIC("opsera.pipeline.ansible.response"), ANSIBLE_STATUS_TOPIC("opsera.pipeline.ansible.status"),
    ANSIBLE_LOG_TOPIC("opsera.pipeline.ansible.log");

    private String topicName;

    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }

}
