package com.opsera.ansible.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opsera.ansible.config.AppConfig;
import com.opsera.ansible.config.IServiceFactory;
import com.opsera.ansible.kafka.KafkaMessageRequest;
import com.opsera.ansible.kafka.KafkaTopics;

@Component
public class KafkaHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaHelper.class);

    @Autowired
    private IServiceFactory serviceFactory;

    @Autowired
    private AppConfig appConfig;

    private static final String KAFKA_PUB_URL = "/v1.0/publishMessage";
    private static final String SUCCESS = "SUCCESS";

    /**
     * Method to publish to Kafka topic via Kafka Integrator Service
     * 
     * @param request
     * @return
     */
    public String postNotificationToKafkaService(KafkaTopics topic, String message) {
        String url = appConfig.getKafkaBaseUrl() + KAFKA_PUB_URL;
        KafkaMessageRequest request = new KafkaMessageRequest();
        request.setKafkaTopicName(topic);
        request.setMessage(message);
        try {
            String responseEntity = serviceFactory.getRestTemplate().postForObject(url, request, String.class);
            LOGGER.info(responseEntity);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while sending message to topic {}", topic.getTopicName(), e);
        }
        return SUCCESS;
    }
}