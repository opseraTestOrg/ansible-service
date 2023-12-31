/**
 * 
 */
package com.opsera.ansible.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opsera.ansible.client.command.PlaybookCommand;
import com.opsera.ansible.client.util.AnsibleClient;
import com.opsera.ansible.client.util.ReturnValue;
import com.opsera.ansible.client.util.ReturnValue.Result;
import com.opsera.ansible.config.ServiceFactory;
import com.opsera.ansible.dto.AnsibleConnectionClientRequest;
import com.opsera.ansible.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.dto.ToolsConfigDetails;
import com.opsera.ansible.dto.ToolsConfigurations;
import com.opsera.ansible.dto.VaultConfig;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.kafka.KafkaTopics;
import com.opsera.ansible.resources.AnsibleKafkaConstants;
import com.opsera.ansible.resources.AnsiblePayloadRequestConfig;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.AnsibleServiceFactory.AnsibleServiceType;
import com.opsera.ansible.service.impl.GenericPlaybookServiceImpl;
import com.opsera.ansible.util.AnsibleUtility;
import com.opsera.ansible.util.ClientUtility;
import com.opsera.ansible.util.JobStatus;

/**
 * @author sreeni
 *
 */
@Service
public class CommandService {


    public static final Logger LOGGER = LoggerFactory.getLogger(CommandService.class);

    @Autowired
    private ServiceFactory serviceFactory;

    @Autowired
    AnsibleServiceFactory ansibleServiceFactory;
    
    @Autowired
    GenericPlaybookServiceImpl playbookService;

    @Autowired
    private ClientUtility clientUtility;

    @Autowired
    private AnsibleUtility ansibleUtility;

    /** The kafka helper. */
    @Autowired
    private com.opsera.core.helper.KafkaHelper kafkaHelper;

    @Autowired
    private ToolConfigurationService toolConfigurationService;

    /**
     * @param ansibleClientRequest AnsibleClientRequest
     * @return Map<String, String> errors
     */
    public Map<String, ReturnValue> validatePayloadforCommand(AnsibleConnectionClientRequest ansibleClientRequest) {
        Map<String, String> errors = new HashMap<>();
        try {
            if (ansibleClientRequest == null) {
                errors.put(AnsibleServiceConstants.ERROR_ANS400, AnsibleServiceConstants.ERROR_IN_INPUT_PAYLOAD);
            } else {
                if (ansibleClientRequest.getHostName() == null || (ansibleClientRequest.getHostName() != null && ansibleClientRequest.getHostName().isEmpty())) {
                    errors.put(AnsibleServiceConstants.ERROR_ANS401, AnsibleServiceConstants.HOST_NAME_SHOULD_NOT_BE_EMPTY_ERROR);
                } else if (!isValidIPAddress(ansibleClientRequest.getHostName())) {
                    errors.put(AnsibleServiceConstants.ERROR_ANS402, AnsibleServiceConstants.HOST_NAME_IP_SHOULD_BE_VALID_ERROR);
                }

                if (!(ansibleClientRequest.getPort() == 22 || ansibleClientRequest.getPort() == 21)) {
                    errors.put(AnsibleServiceConstants.ERROR_ANS403, AnsibleServiceConstants.PORT_SHOULD_BE_EITHER_22_OR_21_ERROR);
                }
                /*
                 * if (ansibleClientRequest.getPubKeyPath() == null ||
                 * (ansibleClientRequest.getPubKeyPath() != null &&
                 * ansibleClientRequest.getPubKeyPath().isEmpty())) {
                 * errors.put(AnsibleServiceConstants.ERROR_ANS404, AnsibleServiceConstants.
                 * PUBKEY_PATH_IS_MANDATORY_AND_SHOULD_NOT_BE_EMPTY_ERROR); }
                 */
                if (ansibleClientRequest.getUserName() == null || (ansibleClientRequest.getUserName() != null && ansibleClientRequest.getUserName().isEmpty())) {
                    errors.put(AnsibleServiceConstants.ERROR_ANS405, AnsibleServiceConstants.USERNAME_IS_MANDATORY_AND_SHOULD_NOT_BE_EMPTY_ERROR);
                }
            }
            return getFormattedErrorResponse(errors, ansibleClientRequest);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_INPUT_MSG_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_INPUT_ERROR + ex.getMessage());
        }
    }

    /**
     * responseMap is failure
     * 
     * @param Map<String,                    ReturnValue> responseMap
     * @param AnsibleConnectionClientRequest ansibleClientRequest
     * @return Map<String, AnsiblePlayBookResponseDto> ansibleCustomResponse
     */
    public Map<String, AnsiblePlayBookResponseDto> connectionResponseValidation(Map<String, ReturnValue> responseMap, AnsibleConnectionClientRequest ansibleClientRequest) {

        Map<String, AnsiblePlayBookResponseDto> ansibleCustomResponse = new HashMap<>();
        try {
            if (responseMap.isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                errors.put(AnsibleServiceConstants.ERROR_ANS410, AnsibleServiceConstants.CLIENT_CREATION_FAILURE_ERROR);
                return getFormattedErrorResponseForPingCommand(errors, ansibleClientRequest);
            } else {
                if (responseMap != null) {
                    responseMap.entrySet().parallelStream().filter(Objects::nonNull).forEach(ansibleResInstance -> {
                        String stautsForServer = ansibleResInstance.getKey();
                        Object valueRef = ansibleResInstance.getValue();
                        if (valueRef instanceof ReturnValue) {
                            AnsiblePlayBookResponseDto ansibleCommandLineResp = new AnsiblePlayBookResponseDto();
                            ansibleCommandLineResp.setRc(((ReturnValue) valueRef).getRc());
                            ansibleCommandLineResp.setResult(((ReturnValue) valueRef).getResult().toString());
                            ansibleCommandLineResp.setSuccess(((ReturnValue) valueRef).isSuccess());
                            ansibleCommandLineResp.setStdout(((ReturnValue) valueRef).getStdout());

                            if (((ReturnValue) valueRef).getResult() == Result.unmanaged) {
                                ansibleCommandLineResp.setSuccess(true);
                            }
                            ;
                            ansibleCustomResponse.put(stautsForServer, ansibleCommandLineResp);
                        } else if (valueRef instanceof Map<?, ?>) {
                            if (!((Map<?, ?>) valueRef).isEmpty()) {
                                AnsiblePlayBookResponseDto ansibleCommandLineResp = new AnsiblePlayBookResponseDto();
                                ansibleCommandLineResp.setRc((int) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RC));
                                ansibleCommandLineResp.setResult((String) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RESULT));
                                ansibleCommandLineResp.setSuccess((Boolean) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_SUCCESS));
                                ansibleCommandLineResp.setStdout((List<String>) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_STDOUT));
                                if (((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RESULT) == Result.unmanaged) {
                                    ansibleCommandLineResp.setSuccess(true);
                                }
                                ;
                                ansibleCustomResponse.put(stautsForServer, ansibleCommandLineResp);
                            }
                        }

                    });
                }
            }

            return ansibleCustomResponse;
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_PING_RESPONSE_MSG_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_PING_RESPONSE_ERROR + ex.getMessage());
        }
    }

    /**
     * @param Map<String,                    String> errorsMap
     * @param AnsibleConnectionClientRequest ansibleClientRequest
     * @return Map<String, AnsiblePlayBookResponseDto> errorMap
     */
    private Map<String, AnsiblePlayBookResponseDto> getFormattedErrorResponseForPingCommand(Map<String, String> errorsMap, AnsibleConnectionClientRequest ansibleClientRequest) {
        Map<String, AnsiblePlayBookResponseDto> errorMap = new HashMap<String, AnsiblePlayBookResponseDto>();
        try {

            if (!errorsMap.isEmpty()) {
                AnsiblePlayBookResponseDto returnValue = new AnsiblePlayBookResponseDto();
                returnValue.setResult(Result.failed.toString());
                List<String> errorMsgs = new ArrayList<>();
                errorsMap.entrySet().parallelStream().filter(Objects::nonNull).forEach(pair -> {
                    errorMsgs.add(pair.getKey() + AnsibleServiceConstants.ERROR_FORMAT_HYPHEN + pair.getValue());
                });

                returnValue.setStdout(errorMsgs);
                returnValue.setSuccess(false);
                errorMap.put(ansibleClientRequest.getHostName(), returnValue);

            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_FORMATTING_CUSTOM_PING_FAILURE_RESPONSE_MSG_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_FORMATTING_CUSTOM_PING_FAILURE_RESPONSE_ERROR + ex.getMessage());
        }
        return errorMap;
    }

    /**
     * @param Map<String,                    String> errorsMap
     * @param AnsibleConnectionClientRequest ansibleClientRequest
     * @return Map<String, ReturnValue> errorMap
     */
    public Map<String, ReturnValue> getFormattedErrorResponse(Map<String, String> errorsMap, AnsibleConnectionClientRequest ansibleClientRequest) {
        Map<String, ReturnValue> errorMap = new HashMap<String, ReturnValue>();
        try {

            if (!errorsMap.isEmpty()) {
                ReturnValue returnValue = new ReturnValue();
                returnValue.setResult(Result.failed);
                List<String> errorMsgs = new ArrayList<>();
                errorsMap.entrySet().parallelStream().filter(Objects::nonNull).forEach(pair -> {
                    errorMsgs.add(pair.getKey() + AnsibleServiceConstants.ERROR_FORMAT_HYPHEN + pair.getValue());
                });

                returnValue.setStdout(errorMsgs);

                errorMap.put(ansibleClientRequest.getHostName(), returnValue);

            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_FORMATTING_RESPONSE_MSG_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_FORMATTING_RESPONSE_ERROR + ex.getMessage());
        }
        return errorMap;
    }

    /**
     * @param AnsiblePlayBookClientRequest ansiblePlayBookClientRequest
     * @return Map<String, ReturnValue> errorMap
     */
    public Map<String, ReturnValue> validatePayloadforFileCreation(AnsiblePlayBookClientRequest ansiblePlayBookClientRequest) {
        Map<String, ReturnValue> errorMap = new HashMap<String, ReturnValue>();
        try {
            errorMap = validatePayloadforCommand(ansiblePlayBookClientRequest.getAnsibleClientRequest());

//            Map<String, String> argMap = ansiblePlayBookClientRequest.getCommandArgs();
            Map<String, String> errors = new HashMap<>();

//            if (!(argMap.containsKey(AnsibleServiceConstants.FILEPATH) && argMap.containsKey(AnsibleServiceConstants.FILENAME) && argMap.containsKey(AnsibleServiceConstants.COMMAND))) {
//                errors.put(AnsibleServiceConstants.ERROR_ANS406, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_ATTRIBUTE_ERROR);
//            }
//
//            if ((argMap.get(AnsibleServiceConstants.FILEPATH) != null && argMap.get(AnsibleServiceConstants.FILEPATH).isEmpty())) {
//                errors.put(AnsibleServiceConstants.ERROR_ANS407, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_FILEPATH_VALUE_ERROR);
//            }
//
//            if ((argMap.get(AnsibleServiceConstants.FILENAME) != null && argMap.get(AnsibleServiceConstants.FILENAME).isEmpty())) {
//                errors.put(AnsibleServiceConstants.ERROR_ANS408, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_FILENAME_VALUE_ERROR);
//            }
//
//            if ((argMap.get(AnsibleServiceConstants.COMMAND) != null && argMap.get(AnsibleServiceConstants.COMMAND).isEmpty())) {
//                errors.put(AnsibleServiceConstants.ERROR_ANS409, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_COMMAND_VALUE_ERROR);
//            }

            if (errorMap.isEmpty()) {
                errorMap = getFormattedErrorResponse(errors, ansiblePlayBookClientRequest.getAnsibleClientRequest());
            }

        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_FILE_CREATION_PAYLOAD_INPUT_MSG_ERROR, serviceFactory.gson().toJson(ansiblePlayBookClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_FILE_CREATION_PAYLOAD_INPUT_ERROR + ex.getMessage());
        }
        return errorMap;
    }

    /**
     * @param ansibleClient          AnsibleClient
     * @param ansiblePlayBookRequest AnsiblePlayBookClientRequest
     */
    public void downloadFilesFromGithub(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        try {
            runPlaybook(ansibleClient, ansiblePlayBookRequest, AnsibleServiceType.DownloadFromGit);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DOWNLOAD_GITHUB_FOLDER_PLAYBOOK_ANSIBLE_SERVER_MSG_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DOWNLOAD_GITHUB_FOLDER_PLAYBOOK_ANSIBLE_SERVER_ERROR + ex.getMessage());
        }
    }

    /**
     * @param ansibleClient          AnsibleClient
     * @param ansiblePlayBookRequest AnsiblePlayBookClientRequest
     * @return Map<String, ReturnValue> result
     */
    public Map<String, ReturnValue> executePlaybookService(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {
        Map<String, ReturnValue> result = new HashMap<>();

        try {
            result = runPlaybook(ansibleClient, ansiblePlayBookRequest, AnsibleServiceType.ExecutePlaybook);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_SERVICE_ANSIBLE_SERVER_MSG_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_SERVICE_ANSIBLE_SERVER_ERROR + ex.getMessage());
        }

        return result;

    }

    /**
     * @param AnsibleClient                ansibleClient
     * @param AnsiblePlayBookClientRequest ansiblePlayBookRequest
     */
    public void deleteGitCheckoutFiles(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        try {
            runPlaybook(ansibleClient, ansiblePlayBookRequest, AnsibleServiceType.DeleteGitCheckoutFolder);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DELETE_CHECKOUT_FOLDER_PLAYBOOK_ANSIBLE_SERVER_MSG_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DELETE_CHECKOUT_FOLDER_PLAYBOOK_ANSIBLE_SERVER_ERROR + ex.getMessage());
        }

    }

    /**
     * @param AnsiblePayloadRequestConfig ansiblePayloadRequestConfig
     */
    public void executePlaybook(AnsiblePayloadRequestConfig ansiblePayloadRequestConfig) {
        LOGGER.info("Inside Exceute Playbook begining {}" , serviceFactory.gson().toJson(ansiblePayloadRequestConfig));
        String pipelineId = ansiblePayloadRequestConfig.getPipelineId();
        String stepId = ansiblePayloadRequestConfig.getStepId();
        String customerId = ansiblePayloadRequestConfig.getCustomerId();
        int runCount = ansiblePayloadRequestConfig.getRunCount();
        Map<String, JobStatus> jobStatus=new HashMap<>();
        Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse = new HashMap<String, AnsiblePlayBookResponseDto>();
        LOGGER.info(AnsibleKafkaConstants.ANSIBLE_REQUEST_FOR_RESPONSE_TOPIC);
        kafkaHelper.postNotificationToKafka(KafkaTopics.OPSERA_PIPELINE_RESPONSE.getTopicName(), serviceFactory.gson()
                .toJson(ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.RUNNING.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_RECEIVED, runCount)));
        try {
            AnsibleService ansibleService = ansibleServiceFactory.getAnsibleService(AnsibleServiceType.ExecutePlaybook);
            AnsiblePlayBookClientRequest ansiblePlayBookClientRequest = ansibleService.getAnsiblePlaybookRequestFromKafka(ansiblePayloadRequestConfig);
            ansiblePlayBookClientRequest = setAnsibleClientConnectionDetails(ansiblePayloadRequestConfig, ansiblePlayBookClientRequest);
            ansiblecustomResponse = executePlaybookCommandWithArguments(ansiblePlayBookClientRequest);
            jobStatus = ansibleService.getAnsibleJobStatus(ansiblecustomResponse);            
            LOGGER.info(AnsibleServiceConstants.EXECUTING_BEFORE_PUSHING_LOG_KAFKA_INFO, serviceFactory.gson().toJson(ansiblePayloadRequestConfig),serviceFactory.gson().toJson(jobStatus));
            postStatustoKafka(ansiblePayloadRequestConfig, jobStatus, ansiblecustomResponse, false);
            LOGGER.info(AnsibleServiceConstants.EXECUTING_AFTER_PUSHING_LOG_KAFKA_INFO, serviceFactory.gson().toJson(ansiblePayloadRequestConfig),serviceFactory.gson().toJson(jobStatus));
            
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(AnsibleServiceConstants.ERROR_WHILE_EXECUTING_PLAYBOOK_FROM_KAFKA ,ex.getMessage() , serviceFactory.gson().toJson(ansiblePayloadRequestConfig));
            postStatustoKafka(ansiblePayloadRequestConfig, jobStatus, ansiblecustomResponse, true);        }
    }

    /**
     * @param AnsiblePlayBookClientRequest ansiblePlayBookRequest
     */
    public Map<String, AnsiblePlayBookResponseDto> executePlaybookCommandWithArguments(AnsiblePlayBookClientRequest ansiblePlayBookRequest) {
        Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse = new HashMap<>();
        Map<String, ReturnValue> validateErrors = null;
        LOGGER.info(AnsibleServiceConstants.EXECUTE_ANSIBLE_PLAYBOOK_IN_ANSIBLE_SERVER_IN_COMMAND_CONTROLLER_INFO);
        try {
            validateErrors = validatePayloadforFileCreation(ansiblePlayBookRequest);
            if (validateErrors.isEmpty()) {
                AnsibleConnectionClientRequest ansibleClientRequest = ansiblePlayBookRequest.getAnsibleClientRequest();
                AnsibleClient ansibleClient = clientUtility.getClient(ansibleClientRequest);
                ansibleClient.setAnsibleRootPath("");
                downloadFilesFromGithub(ansibleClient, ansiblePlayBookRequest);
                Map<String, ReturnValue> result = executePlaybookService(ansibleClient, ansiblePlayBookRequest);
                deleteGitCheckoutFiles(ansibleClient, ansiblePlayBookRequest);
                ansiblecustomResponse = ansibleUtility.getAnsibleCustomResponse(result);
            } else {
                ansiblecustomResponse = ansibleUtility.getAnsibleCustomResponse(validateErrors);
            }
            LOGGER.info(AnsibleServiceConstants.COMPLETED_TO_EXECUTE_PLAYBOOK_IN_ANSIBLE_SERVER_SUCESSFULLY_INFO);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_WITH_ARGS_THROUGH_ANSIBLE_CLIENT_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_WITH_ARGS_THROUGH_ANSIBLE_CLIENT_ERROR + ex.getMessage());
        }
        return ansiblecustomResponse;
    }

    /**
     * This is getting called from Controller
     * 
     * @param ansibleClient          AnsibleClient
     * @param ansiblePlayBookRequest
     * @param ansibleServiceType
     * @return Map<String, ReturnValue>
     */
    private Map<String, ReturnValue> runPlaybook(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest, AnsibleServiceType ansibleServiceType) {
        Map<String, ReturnValue> result = new HashMap<>();
        try {
            AnsibleService ansibleService = ansibleServiceFactory.getAnsibleService(ansibleServiceType);
            AnsiblePlaybookServerRequestDto ansiblePlaybookServerRequestDto = ansibleService.getAnsiblePlaybookRequest(ansiblePlayBookRequest);
            result = ansibleClient.execute(new PlaybookCommand(Lists.newArrayList(ansiblePlayBookRequest.getAnsibleClientRequest().getHostName()),
                    ansiblePlaybookServerRequestDto.getServerPlaybookPath(), ansiblePlaybookServerRequestDto.getCommandArgs()), AnsibleServiceConstants.TIMEOUT_SERVER);

        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_ANSIBLE_SERVER_MSG_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest), ansibleServiceType);
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_ANSIBLE_SERVER_ERROR + ex.getMessage());
        }
        return result;
    }

    /**
     * @param hostIPAddress
     * @return boolean
     */
    private boolean isValidIPAddress(String hostIPAddress) {
        try {
            String zeroTo255 = AnsibleServiceConstants.REG_EXP_IP_ADDRESS;
            String regex = zeroTo255 + AnsibleServiceConstants.REG_EXP_DOT + zeroTo255 + AnsibleServiceConstants.REG_EXP_DOT + zeroTo255 + AnsibleServiceConstants.REG_EXP_DOT + zeroTo255;
            Pattern p = Pattern.compile(regex);
            if (hostIPAddress == null) {
                return false;
            }
            Matcher m = p.matcher(hostIPAddress);
            return m.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the data from the MongoDB to set the AnsiblePlayBookClientRequest object
     * 
     * @param ansiblePayloadRequestConfig
     * @param ansiblePlayBookRequest
     * @return AnsiblePlayBookClientRequest
     */
    private AnsiblePlayBookClientRequest setAnsibleClientConnectionDetails(AnsiblePayloadRequestConfig ansiblePayloadRequestConfig, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        String toolConfigId = ansiblePayloadRequestConfig.getToolConfigId();
        String customerId = ansiblePayloadRequestConfig.getCustomerId();
        try {

            ToolsConfigurations toolsConfigurations = toolConfigurationService.getToolConfigurationDetails(toolConfigId, customerId);
            System.out.println("ToolsConfigurations ------------------------->"+serviceFactory.gson().toJson(toolsConfigurations));
            if (toolsConfigurations != null) {
                ToolsConfigDetails toolsConfigDetails = toolsConfigurations.getConfiguration();
                VaultConfig valutConfig = toolsConfigDetails.getPublicKey();
                AnsibleConnectionClientRequest ansibleClientRequest = new AnsibleConnectionClientRequest();
                ansibleClientRequest.setHostName(toolsConfigDetails.getHostName());
                try {
                    ansibleClientRequest.setPort(Integer.valueOf(toolsConfigDetails.getPort()));
                } catch (NumberFormatException e) {
                    ansibleClientRequest.setPort(0);
                }
                Map<String, String> secrets = serviceFactory.getVaultHelper().getSecrets(toolsConfigurations.getOwner(), Arrays.asList(valutConfig.getVaultKey()));
                System.out.println("Valut Key---------------------------------->"+secrets.get(valutConfig.getVaultKey()));
                ansibleClientRequest.setPubKeyPath(secrets.get(valutConfig.getVaultKey()));
                ansibleClientRequest.setUserName(toolsConfigDetails.getUserName());
                ansiblePlayBookRequest.setAnsibleClientRequest(ansibleClientRequest);
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.ERROR_WHILE_SETTING_TOOL_CONFIG_DETAILS_FOR_ANSIBLE_CLIENT_MSG_ERROR, serviceFactory.gson().toJson(ansiblePayloadRequestConfig));
            throw new AnsibleServiceException(AnsibleServiceConstants.ERROR_WHILE_SETTING_TOOL_CONFIG_DETAILS_FOR_ANSIBLE_CLIENT_ERROR + ex.getMessage());
        }
        return ansiblePlayBookRequest;
    }

    /**
     * @param ansiblePayloadRequestConfig
     * @param ansiblejobStatusMap
     * @param ansiblecustomResponse
     * @param isError TODO
     */
    public void postStatustoKafka(AnsiblePayloadRequestConfig ansiblePayloadRequestConfig, Map<String, JobStatus> ansiblejobStatusMap, Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse, boolean isError) {
        String pipelineId = ansiblePayloadRequestConfig.getPipelineId();
        String stepId = ansiblePayloadRequestConfig.getStepId();
        String customerId = ansiblePayloadRequestConfig.getCustomerId();
        int runCount = ansiblePayloadRequestConfig.getRunCount();
//        kafkaHelper.postNotificationToKafkaService(KafkaTopics.ANSIBLE_LOG_TOPIC, serviceFactory.gson()
//                .toJson(ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, serviceFactory.gson().toJson(ansiblecustomResponse), AnsibleKafkaConstants.ANSIBLE_REQUEST_SUCESS_FOR_LOG_TOPIC, runCount)));
        if (!isError) {
        LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_SUCCESS_FOR_ANSIBLE_LOG_TOPIC);
        kafkaHelper.postNotificationToKafka(KafkaTopics.OPSERA_PIPELINE_LOG.getTopicName(), serviceFactory.gson().toJson(
                ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.SUCCESS.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_SUCCESS_FOR_LOG_TOPIC + serviceFactory.gson().toJson(ansiblecustomResponse), runCount)));
        
        LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_SUCCESS_FOR_ANSIBLE_STATUS_TOPIC);
        kafkaHelper.postNotificationToKafka(KafkaTopics.OPSERA_PIPELINE_STATUS.getTopicName(), serviceFactory.gson()
                .toJson(ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.SUCCESS.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_SUCCESS_FOR_STATUS_TOPIC , runCount)));
        }else {
            LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_FAILED_FOR_ANSIBLE_LOG_TOPIC);
          kafkaHelper.postNotificationToKafka(KafkaTopics.OPSERA_PIPELINE_LOG.getTopicName(), serviceFactory.gson().toJson(
                  ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.FAILED.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_FAILURE_FOR_LOG_TOPIC + serviceFactory.gson().toJson(ansiblecustomResponse), runCount)));
          
          LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_FAILED_FOR_ANSIBLE_STATUS_TOPIC);
          kafkaHelper.postNotificationToKafka(KafkaTopics.OPSERA_PIPELINE_STATUS.getTopicName(), serviceFactory.gson()
                  .toJson(ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.FAILED.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_FAILURE_FOR_STATUS_TOPIC , runCount)));
        }
        
//        if (ansiblejobStatusMap != null) {
//            ansiblejobStatusMap.entrySet().parallelStream().filter(Objects::nonNull).forEach(ansibleResInstance -> {
//                JobStatus jobStatus = ansibleResInstance.getValue();
//                String hostName = ansibleResInstance.getKey();
//                if (jobStatus != null && jobStatus == JobStatus.SUCCESS) {
//                    LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_SUCCESS_FOR_ANSIBLE_LOG_TOPIC);
//                    kafkaHelper.postNotificationToKafkaService(KafkaTopics.ANSIBLE_LOG_TOPIC, serviceFactory.gson().toJson(
//                            ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, hostName, JobStatus.SUCCESS.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_SUCCESS_FOR_LOG_TOPIC + hostName, runCount)));
//                    
//                    LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_SUCCESS_FOR_ANSIBLE_STATUS_TOPIC);
//                    kafkaHelper.postNotificationToKafkaService(KafkaTopics.ANSIBLE_STATUS_TOPIC, serviceFactory.gson()
//                            .toJson(ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.SUCCESS.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_SUCCESS_FOR_STATUS_TOPIC + hostName, runCount)));
//                    
//                } else {
//                    LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_FAILED_FOR_ANSIBLE_LOG_TOPIC);
//                    kafkaHelper.postNotificationToKafkaService(KafkaTopics.ANSIBLE_LOG_TOPIC, serviceFactory.gson().toJson(
//                            ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, hostName, JobStatus.FAILED.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_FAILURE_FOR_LOG_TOPIC + hostName, runCount)));
//                    
//                    LOGGER.info(AnsibleServiceConstants.INSIDE_JOB_STATUS_FAILED_FOR_ANSIBLE_STATUS_TOPIC);
//                    kafkaHelper.postNotificationToKafkaService(KafkaTopics.ANSIBLE_STATUS_TOPIC, serviceFactory.gson()
//                            .toJson(ansibleUtility.createStepExecutionResponse(pipelineId, stepId, customerId, JobStatus.FAILED.name(), AnsibleKafkaConstants.ANSIBLE_REQUEST_FAILURE_FOR_STATUS_TOPIC + hostName, runCount)));
//                }
//            });
//        }
    }

}
