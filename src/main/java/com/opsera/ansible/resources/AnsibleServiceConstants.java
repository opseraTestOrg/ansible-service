package com.opsera.ansible.resources;

import lombok.experimental.UtilityClass;

/**
 * @author sreeni
 *
 */
@UtilityClass
public class AnsibleServiceConstants {

    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE_RESULT = "result";
    public static final String RESPONSE_RC = "rc";
    public static final Object RESPONSE_STDOUT = "stdout";
    public static final String ERROR_FORMAT_HYPHEN = "-";

    public static final String GIT_PLAYBOOK_PATH = "/etc/ansible/playbooks/gitCheckout_playbook.yml";
    public static final String DELETE_CHECKOUT_PLAYBOOK_PATH = "/etc/ansible/playbooks/gitcheckout_delete_playbook.yml";
    public static final String TEMP__GIT_CHECKOUT_PATH = "/tmp/ansible-checkout/";

    public static final String REG_EXP_DOT = "\\.";
    public static final String REG_EXP_IP_ADDRESS = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";

    public static final int TIMEOUT_SERVER = 30;

    public static final String COMMAND = "command";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";

    /**
     * Info-messages are used in the Ansible execution process.
     */

    public static final String COMPLETED_TO_EXECUTE_PLAYBOOK_IN_ANSIBLE_SERVER_SUCESSFULLY_INFO = "completed to execute playbook in Ansible server sucessfully in CommandController:executePlaybook()";
    public static final String EXECUTE_ANSIBLE_PLAYBOOK_IN_ANSIBLE_SERVER_IN_COMMAND_CONTROLLER_INFO = "Starting to play ansible playbook in Ansible server using  ansible client in CommandController:executePlaybook()";
    public static final String PING_ANSIBLE_SERVER_SUCESSFULLY_IN_COMMAND_CONTROLLER_INFO = "completed to ping Ansible server sucessfully in CommandController:executeCommand()";
    public static final String CREATE_NEW_ANSIBLE_CLIENT_IN_COMMAND_OPERATIONS_CONTROLLER_INFO = "Starting to create new ansible client in CommandController:executeCommand()";
    public static final String COMPLETED_TO_CREATE_NEW_ANSIBLE_CLIENT_USING_ANSIBLE_CLIENT_REQUEST_INFO = "Completed to create new ansible client using AnsibleClientRequest";
    public static final String CREATE_NEW_ANSIBLE_CLIENT_USING_ANSIBLE_CLIENT_REQUEST_INFO = "Starting to create new ansible client using AnsibleClientRequest}";
    public static final String DELETE_CHECKOUT_SERVICE_IMPL_INFO = "Starting to get Ansible server DTO object from DeleteCheckoutFolderServiceImpl::getAnsiblePlaybookRequest";
    public static final String FILE_CREATION_SERVICE_IMPL_INFO = "Starting to get Ansible server DTO object from FileCreationServiceImpl::getAnsiblePlaybookRequest";
    public static final String DOWNLOAD_GIT_CHECKOUT_SERVICE_IMPL_INFO = "Starting to get Ansible server DTO object from DownloadGitServiceImpl::getAnsiblePlaybookRequest";

    /**
     * Error-messages are used in the Ansible execution process.
     */

    public static final String EXECUTING_PLAYBOOK_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR = "Error while executing playbook command through  Ansible Client";
    public static final String EXECUTING_PING_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR = "Error while executing ping command through  Ansible Client";
    public static final String PARSING_PLAY_BOOK_RESPONSE_FROM_ANSIBLE_ERROR = "Error while parsing playbook response received from  Ansible server";
    public static final String EXECUTING_PLAYBOOK_COMMAND_WITH_ARGS_THROUGH_ANSIBLE_CLIENT_ERROR = "Error while executing playbook in Ansible server by passing arguments ";

    public static final String EXECUTING_PING_COMMAND_COMPLETED = "Execution of the ping command completed in ";
    public static final String EXECUTING_PLAYBOOK_COMMAND_COMPLETED = "Execution of the playbook command completed in ";
    
    public static final String ERROR_WHILE_CONNECTING_ANSIBLE_SERVER_ERROR = "Error while connectiong to ansible server";

    /**
     * Error-Codes are used in the validation payload process.
     */

    public static final String ERROR_ANS400 = "ANS400";
    public static final String ERROR_ANS401 = "ANS401";
    public static final String ERROR_ANS402 = "ANS402";
    public static final String ERROR_ANS403 = "ANS403";
    public static final String ERROR_ANS404 = "ANS404";
    public static final String ERROR_ANS405 = "ANS405";
    public static final String ERROR_ANS406 = "ANS406";
    public static final String ERROR_ANS407 = "ANS407";
    public static final String ERROR_ANS408 = "ANS408";
    public static final String ERROR_ANS409 = "ANS409";
    public static final String ERROR_ANS410 = "ANS410";

    /**
     * Error-Messages are used in the validation payload process.
     */

    public static final String USERNAME_IS_MANDATORY_AND_SHOULD_NOT_BE_EMPTY_ERROR = "UserName is mandatory and should not be empty";
    public static final String PUBKEY_PATH_IS_MANDATORY_AND_SHOULD_NOT_BE_EMPTY_ERROR = "Public key path is mandatory and should not be empty";
    public static final String PORT_SHOULD_BE_EITHER_22_OR_21_ERROR = "Port number be either 22 or 21 ";
    public static final String HOST_NAME_IP_SHOULD_BE_VALID_ERROR = "Hostname ip should be valid";
    public static final String HOST_NAME_SHOULD_NOT_BE_EMPTY_ERROR = "Hostname should not be empty";
    public static final String ERROR_IN_INPUT_PAYLOAD = "Error in Input Payload";
    public static final String EXECUTION_FAILED_WHILE_VALIDATING_INPUT_ERROR = "Error in validation of input fields in CommandService::validatePayloadforCommand ";
    public static final String EXECUTION_FAILED_WHILE_VALIDATING_FILE_CREATION_PAYLOAD_INPUT_ERROR = "Error in validation of file creation payload in CommandService::validatePayloadforFileCreation ";;
    public static final String CLIENT_CREATION_FAILURE_ERROR = "java.lang.RuntimeException: create ssh client";

    public static final String PLEASE_PROVIDE_CORRECT_COMMAND_VALUE_ERROR = "Please provide correct command value";
    public static final String PLEASE_PROVIDE_CORRECT_FILENAME_VALUE_ERROR = "Please provide correct filename value";
    public static final String PLEASE_PROVIDE_CORRECT_FILEPATH_VALUE_ERROR = "Please provide correct filepath value";
    public static final String PLEASE_PROVIDE_CORRECT_ATTRIBUTE_ERROR = "Please provide correct attribute";
    public static final String EXECUTION_FAILED_WHILE_FORMATTING_RESPONSE_ERROR = "Error while formatting the error response from input payload";
    public static final String EXECUTION_FAILED_WHILE_CREATING_ANSIBLE_CLIENT = "Error while creating Ansible client";
    public static final String EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_ANSIBLE_SERVER = "Error while executing playbook in ansible server";
    public static final String EXECUTION_FAILED_WHILE_EXECUTING_DELETE_CHECKOUT_FOLDER_PLAYBOOK_ANSIBLE_SERVER = "Error while executing delete checkout folder playbook in ansible server";
    public static final String EXECUTION_FAILED_WHILE_EXECUTING_DOWNLOAD_GITHUB_FOLDER_PLAYBOOK_ANSIBLE_SERVER = "Error while executing download from git hub playbook in ansible server";
    public static final String EXECUTION_FAILED_WHILE_EXECUTING_CREATION_FILE_PLAYBOOK_ANSIBLE_SERVER = "Error while executing creation of file playbook in ansible server";
    public static final String EXECUTING_DELETE_CHECKOUT_SERVICE_IMPL_ERROR = "Error while executing in DeleteCheckoutFolderServiceImpl::getAnsiblePlaybookRequest";
    public static final String EXECUTING_FILE_CREATION_SERVICE_IMPL_ERROR = "Error while executing in FileCreationServiceImpl::getAnsiblePlaybookRequest";
    public static final String EXECUTING_DOWNLOAD_GIT_CHECKOUT_SERVICE_IMPL_ERROR = "Error while executing in DownloadGitServiceImpl::getAnsiblePlaybookRequest";
    public static final String EXECUTION_FAILED_WHILE_GETTING_ANSIBLE_SERVICE_IMPL = "Error while getting ANsible Service Impl in AnsibleServiceFactory::getAnsibleService";
    public static final String EXECUTION_FAILED_WHILE_VALIDATING_PING_RESPONSE_ERROR = "Error while formatting the error response from ping command execution";
    public static final String EXECUTION_FAILED_WHILE_FORMATTING_CUSTOM_PING_FAILURE_RESPONSE_ERROR = "Error while formatting custom failure error response from ping command execution";

}
