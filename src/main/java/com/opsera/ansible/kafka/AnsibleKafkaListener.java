package com.opsera.ansible.kafka;

import static com.opsera.ansible.resources.AnsibleKafkaConstants.ANSIBLE_REQUEST_TOPIC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.opsera.ansible.config.ServiceFactory;
import com.opsera.ansible.resources.AnsibleKafkaConstants;
import com.opsera.ansible.service.AnsibleRequestProcessor;

/**
 * The listener interface for receiving awsKafka events. The class that is
 * interested in processing a awsKafka event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addAwsKafkaListener<code> method. When the awsKafka event
 * occurs, that object's appropriate method is invoked.
 *
 * @see AwsKafkaEvent
 */
/**
 * 
 * @author sreeni
 *
 */
@Service
public class AnsibleKafkaListener {

    /** The task executor. */
    @Autowired
    private TaskExecutor taskExecutor;

    /** The i service factory. */
    @Autowired
    private ServiceFactory iServiceFactory;

    /** The Constant LOGGER. */
    public static final Logger LOGGER = LoggerFactory.getLogger(AnsibleKafkaListener.class);

    /**
     * Consumes AWS Lambda Function Creation Request Details.
     * 
     * @param message the message
     */
    @KafkaListener(topics = ANSIBLE_REQUEST_TOPIC)
    public void processAWSLambdaFunctionCreationRequest(String message) {
        LOGGER.info(AnsibleKafkaConstants.ANSIBLE_REQUEST_RECEIVED_TO_EXECUTE_A_COMMAND, message);
        AnsibleRequestProcessor ansibleRequestProcessor = new AnsibleRequestProcessor(message, iServiceFactory);
        taskExecutor.execute(ansibleRequestProcessor);

    }

}
