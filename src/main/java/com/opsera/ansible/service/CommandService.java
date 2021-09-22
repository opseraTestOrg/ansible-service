/**
 * 
 */
package com.opsera.ansible.service;

import java.util.ArrayList;
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

import com.github.woostju.ansible.AnsibleClient;
import com.github.woostju.ansible.ReturnValue;
import com.github.woostju.ansible.ReturnValue.Result;
import com.github.woostju.ansible.command.PlaybookCommand;
import com.opsera.ansible.config.IServiceFactory;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.request.dto.AnsibleConnectionClientRequest;
import com.opsera.ansible.request.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.request.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.AnsibleServiceFactory.AnsibleServiceType;

/**
 * @author sreeni
 *
 */
@Service
public class CommandService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommandService.class);

    @Autowired
    private IServiceFactory serviceFactory;

    @Autowired
    AnsibleServiceFactory ansibleServiceFactory;

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
                if (ansibleClientRequest.getPubKeyPath() == null || (ansibleClientRequest.getPubKeyPath() != null && ansibleClientRequest.getPubKeyPath().isEmpty())) {
                    errors.put(AnsibleServiceConstants.ERROR_ANS404, AnsibleServiceConstants.PUBKEY_PATH_IS_MANDATORY_AND_SHOULD_NOT_BE_EMPTY_ERROR);
                }
                if (ansibleClientRequest.getUserName() == null || (ansibleClientRequest.getUserName() != null && ansibleClientRequest.getUserName().isEmpty())) {
                    errors.put(AnsibleServiceConstants.ERROR_ANS405, AnsibleServiceConstants.USERNAME_IS_MANDATORY_AND_SHOULD_NOT_BE_EMPTY_ERROR);
                }
            }
            return getFormattedErrorResponse(errors, ansibleClientRequest);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_INPUT_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_INPUT_ERROR + ex.getMessage());
        }
    }

    /**
     * @param Map<String, String>  errorsMap
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
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_FORMATTING_RESPONSE_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
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

            Map<String, String> argMap = ansiblePlayBookClientRequest.getCommandArgs();
            Map<String, String> errors = new HashMap<>();

            if (!(argMap.containsKey(AnsibleServiceConstants.FILEPATH) && argMap.containsKey(AnsibleServiceConstants.FILENAME) && argMap.containsKey(AnsibleServiceConstants.COMMAND))) {
                errors.put(AnsibleServiceConstants.ERROR_ANS406, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_ATTRIBUTE_ERROR);
            }

            if ((argMap.get(AnsibleServiceConstants.FILEPATH) != null && argMap.get(AnsibleServiceConstants.FILEPATH).isEmpty())) {
                errors.put(AnsibleServiceConstants.ERROR_ANS407, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_FILEPATH_VALUE_ERROR);
            }

            if ((argMap.get(AnsibleServiceConstants.FILENAME) != null && argMap.get(AnsibleServiceConstants.FILENAME).isEmpty())) {
                errors.put(AnsibleServiceConstants.ERROR_ANS408, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_FILENAME_VALUE_ERROR);
            }

            if ((argMap.get(AnsibleServiceConstants.COMMAND) != null && argMap.get(AnsibleServiceConstants.COMMAND).isEmpty())) {
                errors.put(AnsibleServiceConstants.ERROR_ANS409, AnsibleServiceConstants.PLEASE_PROVIDE_CORRECT_COMMAND_VALUE_ERROR);
            }

            if (errorMap.isEmpty()) {
                errorMap = getFormattedErrorResponse(errors, ansiblePlayBookClientRequest.getAnsibleClientRequest());
            }

        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_FILE_CREATION_PAYLOAD_INPUT_ERROR, serviceFactory.gson().toJson(ansiblePlayBookClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_VALIDATING_FILE_CREATION_PAYLOAD_INPUT_ERROR + ex.getMessage());
        }
        return errorMap;
    }
    
    /**
     * @param ansibleClient AnsibleClient
     * @param ansiblePlayBookRequest AnsiblePlayBookClientRequest
     */
    public void downloadFilesFromGithub(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        try {
            executePlaybook(ansibleClient, ansiblePlayBookRequest, AnsibleServiceType.DownloadFromGit);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DOWNLOAD_GITHUB_FOLDER_PLAYBOOK_ANSIBLE_SERVER, serviceFactory.gson().toJson(ansiblePlayBookRequest) );
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DOWNLOAD_GITHUB_FOLDER_PLAYBOOK_ANSIBLE_SERVER + ex.getMessage());
        }
    }
    
    /**
     * @param ansibleClient AnsibleClient
     * @param ansiblePlayBookRequest AnsiblePlayBookClientRequest
     * @return Map<String, ReturnValue> result
     */
    public Map<String, ReturnValue> createFile(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {
        Map<String, ReturnValue> result = new HashMap<>();

        try {
            result = executePlaybook(ansibleClient, ansiblePlayBookRequest, ansiblePlayBookRequest.getServiceType());
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_CREATION_FILE_PLAYBOOK_ANSIBLE_SERVER, serviceFactory.gson().toJson(ansiblePlayBookRequest) );
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_CREATION_FILE_PLAYBOOK_ANSIBLE_SERVER + ex.getMessage());
        }
        
        return result;

    }

    /**
     * @param AnsibleClient ansibleClient
     * @param AnsiblePlayBookClientRequest ansiblePlayBookRequest
     */
    public void deleteGitCheckoutFiles(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        try {
            executePlaybook(ansibleClient, ansiblePlayBookRequest, AnsibleServiceType.DeleteGitCheckoutFolder);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DELETE_CHECKOUT_FOLDER_PLAYBOOK_ANSIBLE_SERVER, serviceFactory.gson().toJson(ansiblePlayBookRequest) );
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_DELETE_CHECKOUT_FOLDER_PLAYBOOK_ANSIBLE_SERVER + ex.getMessage());
        }

    }

    /**
     * @param ansibleClient AnsibleClient
     * @param ansiblePlayBookRequest
     * @param ansibleServiceType
     * @return Map<String, ReturnValue>
     */
    private Map<String, ReturnValue> executePlaybook(AnsibleClient ansibleClient, AnsiblePlayBookClientRequest ansiblePlayBookRequest, AnsibleServiceType ansibleServiceType) {
        Map<String, ReturnValue> result = new HashMap<>();
        try {
            AnsibleService ansibleService = ansibleServiceFactory.getAnsibleService(ansibleServiceType);
            AnsiblePlaybookServerRequestDto ansiblePlaybookServerRequestDto = ansibleService.getAnsiblePlaybookRequest(ansiblePlayBookRequest);
            result = ansibleClient.execute(new PlaybookCommand(Lists.newArrayList(ansiblePlayBookRequest.getAnsibleClientRequest().getHostName()),
                    ansiblePlaybookServerRequestDto.getServerPlaybookPath(), ansiblePlaybookServerRequestDto.getCommandArgs()), AnsibleServiceConstants.TIMEOUT_SERVER);

        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_ANSIBLE_SERVER, serviceFactory.gson().toJson(ansiblePlayBookRequest) , ansibleServiceType);
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_EXECUTING_PLAYBOOK_ANSIBLE_SERVER + ex.getMessage());
        }
        return result;
    }

    /**
     * @param hostIPAddress
     * @return boolean
     */
    private boolean isValidIPAddress(String hostIPAddress) {
        String zeroTo255 = AnsibleServiceConstants.REG_EXP_IP_ADDRESS;
        String regex = zeroTo255 + AnsibleServiceConstants.REG_EXP_DOT + zeroTo255 + AnsibleServiceConstants.REG_EXP_DOT + zeroTo255 + AnsibleServiceConstants.REG_EXP_DOT + zeroTo255;
        Pattern p = Pattern.compile(regex);
        if (hostIPAddress == null) {
            return false;
        }
        Matcher m = p.matcher(hostIPAddress);
        return m.matches();
    }

   

}
