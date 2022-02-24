package com.opsera.ansible.kafka;

import lombok.Getter;

/**
 * 
 * @author sreeni This class is used to maintain the kafka topics required for
 *         the ansible integration
 */
@Getter
public enum KafkaTopics {

    OPSERA_PIPELINE_RESPONSE("opsera.pipeline.response"), OPSERA_PIPELINE_STATUS("opsera.pipeline.status"), OPSERA_PIPELINE_LOG("opsera.pipeline.log");

    private String topicName;

    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }

}
