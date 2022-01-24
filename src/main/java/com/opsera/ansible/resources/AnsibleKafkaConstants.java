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
    public static final String ANSIBLE_REQUEST_RECEIVED ="Ansible Request received and its under prorcessing";
    public static final String ANSIBLE_REQUEST_SUCESS="Success";
    public static final String ANSIBLE_REQUEST_FAILURE="Failure";
    public static final String ANSIBLE_REQUEST_SUCCESS_FOR_LOG_TOPIC = "In Log topic with success status  and Request was succefully processed for machine name : ";
    public static final String ANSIBLE_REQUEST_SUCCESS_FOR_STATUS_TOPIC = "In Status topic with success status and Playbook Request was succefully processed for machine name : ";
    public static final String ANSIBLE_REQUEST_FAILURE_FOR_LOG_TOPIC = "In Log topic with failure status and Request was succefully processed for machine name : ";
    public static final String ANSIBLE_REQUEST_FAILURE_FOR_STATUS_TOPIC = "In Status topic with failure status and Request was succefully processed for machine name : ";
    public static final String ANSIBLE_REQUEST_FOR_RESPONSE_TOPIC = "In response topic , Ansible Request received and its under prorcessing ";
    

}
