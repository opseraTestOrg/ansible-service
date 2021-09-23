package com.opsera.ansible.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opsera.ansible.config.IServiceFactory;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.request.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.request.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.AnsibleService;

/**
 * @author sreeni
 *
 */
public class FileCreationServiceImpl implements AnsibleService {

    @Autowired
    private IServiceFactory serviceFactory;

    public static final Logger LOGGER = LoggerFactory.getLogger(FileCreationServiceImpl.class);

    /**
     * This method used to create an AnsiblePlaybookServerRequestDto for
     * FileCreationServiceImpl
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
                Map<String, String> argMap = ansiblePlayBookRequest.getCommandArgs();
                List<String> options = new ArrayList<>();
                options.add("-e");
                options.add("\"");
                options.add("destination=" + argMap.get("filepath") + argMap.get("filename"));
                options.add("\"");
                ansiblePlaybookServerRequestDto.setCommandArgs(options);
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_FILE_CREATION_SERVICE_IMPL_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_FILE_CREATION_SERVICE_IMPL_ERROR + ex.getMessage());
        }

        return ansiblePlaybookServerRequestDto;
    }

}
