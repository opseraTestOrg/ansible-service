package com.opsera.ansible.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opsera.ansible.config.ServiceFactory;
import com.opsera.ansible.resources.AnsiblePayloadRequestConfig;

public class AnsibleRequestProcessor implements Runnable {
    public static final Logger LOGGER = LoggerFactory.getLogger(AnsibleRequestProcessor.class);

    private String message;

    private ServiceFactory iServiceFactory;

    /**
     *
     * Constructor for setting factory and message objects
     *
     * @param message         String
     * @param iServiceFactory IServiceFactory
     */
    public AnsibleRequestProcessor(String message, ServiceFactory iServiceFactory) {
        this.message = message;
        this.iServiceFactory = iServiceFactory;
    }

    /**
     *
     * thread execution for pushing logs
     *
     */
    @Override
    public void run() {
        AnsiblePayloadRequestConfig ansiblePayloadRequestConfig = iServiceFactory.gson().fromJson(message, AnsiblePayloadRequestConfig.class);
        LOGGER.info("Deserialized Message: {}", ansiblePayloadRequestConfig);
        iServiceFactory.getCommandService().executePlaybook(ansiblePayloadRequestConfig);
    }
}
