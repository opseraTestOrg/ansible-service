/**
 * 
 */
package com.opsera.ansible.resources;

import lombok.experimental.UtilityClass;
/**
 * 
 * @author sreeni
 *This class is used to maintain the constants for kafka related
 */
@UtilityClass
public class AnsibleKafkaConstants {

    public static final String ANSIBLE_REQUEST_TOPIC = "opsera.pipeline.ansible.request";
    public static final String ANSIBLE_RESPONSE_TOPIC = "opsera.pipeline.ansible.response";
    public static final String ANSIBLE_STATUS_TOPIC = "opsera.pipeline.ansible.status";
    public static final String ANSIBLE_LOG_TOPIC = "opsera.pipeline.ansible.log";
    public static final String ANSIBLE_CONSOLE_LOG_TOPIC = "opsera.pipeline.ansible.console.log";
    public static final String ANSIBLE_REQUEST_RECEIVED_TO_EXECUTE_A_COMMAND ="Received a message to execute an ansible command";

}
