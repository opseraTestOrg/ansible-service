package com.opsera.ansible.service.impl;

import java.util.ArrayList;
import java.util.List;

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
public class DownloadGitServiceImpl implements AnsibleService {

    @Autowired
    private IServiceFactory serviceFactory;

    public static final Logger LOGGER = LoggerFactory.getLogger(FileCreationServiceImpl.class);

    /**
     * This method used to create an AnsiblePlaybookServerRequestDto for
     * DownloadGitServiceImpl
     */
    @Override
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        LOGGER.info(AnsibleServiceConstants.DOWNLOAD_GIT_CHECKOUT_SERVICE_IMPL_INFO);
        AnsiblePlaybookServerRequestDto ansiblePlaybookServerRequestDto = new AnsiblePlaybookServerRequestDto();
        try {
            if (ansiblePlayBookRequest != null) {
                ansiblePlaybookServerRequestDto.setAnsibleClientRequest(ansiblePlayBookRequest.getAnsibleClientRequest());
                ansiblePlaybookServerRequestDto.setServerPlaybookPath(AnsibleServiceConstants.GIT_PLAYBOOK_PATH);
                List<String> options = new ArrayList<>();
                options.add("-e");
                options.add("\"");
                options.add("gitRepository=" + ansiblePlayBookRequest.getGitRepositoryPath());
                options.add("gitCheckoutDir=" + ansiblePlayBookRequest.getGitCheckoutPath());
                options.add("\"");
                ansiblePlaybookServerRequestDto.setCommandArgs(options);
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_DOWNLOAD_GIT_CHECKOUT_SERVICE_IMPL_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_DOWNLOAD_GIT_CHECKOUT_SERVICE_IMPL_ERROR + ex.getMessage());
        }

        return ansiblePlaybookServerRequestDto;
    }

}
