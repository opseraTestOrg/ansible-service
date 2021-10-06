package com.opsera.ansible.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opsera.ansible.client.util.ReturnValue;
import com.opsera.ansible.config.IServiceFactory;
import com.opsera.ansible.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.kafka.StepExecutionResponse;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.ToolConfigurationService;

/**
 * @author sreeni
 *
 */
@Component
public class AnsibleUtility {

    public static final Logger LOGGER = LoggerFactory.getLogger(AnsibleUtility.class);

    @Autowired
    IServiceFactory serviceFactory;

    @Autowired
    private ToolConfigurationService toolConfigurationService;

    /**
     * 
     * @param ansibleResponse Map<String, ReturnValue>
     * @return Map<String, AnsiblePlaybookResponseDto>
     */
    public Map<String, AnsiblePlayBookResponseDto> getAnsibleCustomResponse(Map<String, ReturnValue> ansibleResponse) {
        Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse = new HashMap<>();
        try {
            if (ansibleResponse != null) {
                ansibleResponse.entrySet().parallelStream().filter(Objects::nonNull).forEach(ansibleResInstance -> {
                    String stautsForServer = ansibleResInstance.getKey();
                    Object valueRef = ansibleResInstance.getValue();
                    if (valueRef instanceof ReturnValue) {
                        AnsiblePlayBookResponseDto ansibleCommandLineResp = new AnsiblePlayBookResponseDto();
                        ansibleCommandLineResp.setRc(((ReturnValue) valueRef).getRc());
                        ansibleCommandLineResp.setResult(((ReturnValue) valueRef).getResult().toString());
                        ansibleCommandLineResp.setSuccess(((ReturnValue) valueRef).isSuccess());
                        ansibleCommandLineResp.setStdout(((ReturnValue) valueRef).getStdout());
                        ansiblecustomResponse.put(stautsForServer, ansibleCommandLineResp);
                    } else if (valueRef instanceof Map<?, ?>) {
                        if (!((Map<?, ?>) valueRef).isEmpty()) {
                            AnsiblePlayBookResponseDto ansibleCommandLineResp = new AnsiblePlayBookResponseDto();
                            ansibleCommandLineResp.setRc((int) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RC));
                            ansibleCommandLineResp.setResult((String) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RESULT));
                            ansibleCommandLineResp.setSuccess((Boolean) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_SUCCESS));
                            ansibleCommandLineResp.setStdout((List<String>) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_STDOUT));
                            ansiblecustomResponse.put(stautsForServer, ansibleCommandLineResp);
                        }
                    }

                });
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.PARSING_PLAY_BOOK_RESPONSE_FROM_ANSIBLE_ERROR, serviceFactory.gson().toJson(ansibleResponse));
            throw new AnsibleServiceException(AnsibleServiceConstants.PARSING_PLAY_BOOK_RESPONSE_FROM_ANSIBLE_ERROR + ex.getMessage());
        }
        return ansiblecustomResponse;

    }

    /**
     * This method is used to create a status message that is required to post a
     * message on multiple topics for tracking
     * 
     * @param pipelineId String
     * @param stepId     String
     * @param customerId String
     * @param status     String
     * @param message    String
     * @param runCount   int
     * @return StepExecutionResponse stepExecutionResponse
     */
    public StepExecutionResponse createStepExecutionResponse(String pipelineId, String stepId, String customerId, String status, String message, int runCount) {
        return StepExecutionResponse.builder().customerId(customerId).pipelineId(pipelineId).stepId(stepId).runCount(runCount).status(status).message(message).build();
    }
    
    /**
     * This method is used to create a status message that is required to post a
     * message on multiple topics for tracking
     * 
     * @param pipelineId String
     * @param stepId     String
     * @param customerId String
     * @param status     String
     * @param message    String
     * @param message    host
     * @param runCount   int
     * @return StepExecutionResponse stepExecutionResponse
     */
    public StepExecutionResponse createStepExecutionResponse(String pipelineId, String stepId, String customerId,String host, String status, String message, int runCount) {
        return StepExecutionResponse.builder().customerId(customerId).pipelineId(pipelineId).stepId(stepId).runCount(runCount).status(status).message(message).build();
    }

}
