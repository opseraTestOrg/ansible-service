package com.opsera.ansible.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.opsera.ansible.client.util.ReturnValue.Result;
import com.opsera.ansible.config.ServiceFactory;
import com.opsera.ansible.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.resources.AnsiblePayloadRequestConfig;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.AnsibleService;
import com.opsera.ansible.service.AnsibleServiceFactory.AnsibleServiceType;
import com.opsera.ansible.util.JobStatus;

/**
 * @author sreeni
 *
 */
@Service
@Component
public class GenericPlaybookServiceImpl implements AnsibleService {

    @Autowired
    private ServiceFactory serviceFactory;


    public static final Logger LOGGER = LoggerFactory.getLogger(GenericPlaybookServiceImpl.class);

    /**
     * This method used to create an AnsiblePlaybookServerRequestDto for
     * GenericPlaybookServiceImpl
     */
    @Override
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        AnsiblePlaybookServerRequestDto ansiblePlaybookServerRequestDto = new AnsiblePlaybookServerRequestDto();
        try {
            LOGGER.info(AnsibleServiceConstants.FILE_CREATION_SERVICE_IMPL_INFO);

            if (ansiblePlayBookRequest != null) {
                ansiblePlaybookServerRequestDto.setAnsibleClientRequest(ansiblePlayBookRequest.getAnsibleClientRequest());
                String localPlaybookPath = AnsibleServiceConstants.TEMP__GIT_CHECKOUT_PATH + ansiblePlayBookRequest.getGitFileLocation() + "//" + ansiblePlayBookRequest.getGitFileName();
                ansiblePlaybookServerRequestDto.setServerPlaybookPath(localPlaybookPath);
                /*
                 * Map<String, String> argMap = ansiblePlayBookRequest.getCommandArgs();
                 * List<String> options = new ArrayList<>(); options.add("-e");
                 * options.add("\""); options.add("destination=" + argMap.get("filepath") +
                 * argMap.get("filename")); options.add("\"");
                 * ansiblePlaybookServerRequestDto.setCommandArgs(options);
                 */
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_FILE_CREATION_SERVICE_IMPL_MSG_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_FILE_CREATION_SERVICE_IMPL_ERROR + ex.getMessage());
        }

        return ansiblePlaybookServerRequestDto;
    }

    @Override
    public AnsiblePlayBookClientRequest getAnsiblePlaybookRequestFromKafka(AnsiblePayloadRequestConfig ansiblePayloadRequestConfig) {
        AnsiblePlayBookClientRequest ansiblePlayBookRequest = new AnsiblePlayBookClientRequest();
//        ansiblePlayBookRequest.setCommandArgs(ansiblePayloadRequestConfig.getCommandArgs());
        ansiblePlayBookRequest.setGitFileLocation(ansiblePayloadRequestConfig.getGitFileLocation());
        ansiblePlayBookRequest.setGitFileName(ansiblePayloadRequestConfig.getGitFileName());
//        ansiblePlayBookRequest.setServiceType(AnsibleServiceType.valueOf(ansiblePayloadRequestConfig.getServiceType().toString()));

        return ansiblePlayBookRequest;
    }

    
    @Override
    public Map<String, JobStatus> getAnsibleJobStatus(Map<String, AnsiblePlayBookResponseDto> ansibleResponse) {
        Map<String, JobStatus> ansiblejobStatusMap = new HashMap<>();
        try {
            if (ansibleResponse != null) {
                ansibleResponse.entrySet().parallelStream().filter(Objects::nonNull).forEach(ansibleResInstance -> {
                    String key = ansibleResInstance.getKey();
                    AnsiblePlayBookResponseDto ansiblePlayBookResponseDto = ansibleResInstance.getValue();

                    if (!ansiblePlayBookResponseDto.getResult().equals(Result.success.toString())) {
                        ansiblejobStatusMap.put(key, JobStatus.FAILED);
                    } else {
                        ansiblejobStatusMap.put(key, JobStatus.SUCCESS);
                    }

                });
            }

        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.PARSING_PLAY_BOOK_RESPONSE_FROM_ANSIBLE_ERROR, serviceFactory.gson().toJson(ansibleResponse));
            throw new AnsibleServiceException(AnsibleServiceConstants.PARSING_PLAY_BOOK_RESPONSE_FROM_ANSIBLE_ERROR + ex.getMessage());
        }
        return ansiblejobStatusMap;

    }

}
