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

//TODO loggers and exception handling
/**
 * @author sreeni
 *
 */
public class DeleteCheckoutFolderServiceImpl implements AnsibleService {

    @Autowired
    private IServiceFactory serviceFactory;

    public static final Logger LOGGER = LoggerFactory.getLogger(DeleteCheckoutFolderServiceImpl.class);

    /**
     * This method used to create an AnsiblePlaybookServerRequestDto for
     * DeleteCheckoutFolderServiceImpl
     */
    @Override
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest) {

        AnsiblePlaybookServerRequestDto ansibleFileCreationRequestDto = new AnsiblePlaybookServerRequestDto();
        try {
            LOGGER.info(AnsibleServiceConstants.DELETE_CHECKOUT_SERVICE_IMPL_INFO);
            if (ansiblePlayBookRequest != null) {
                ansibleFileCreationRequestDto.setAnsibleClientRequest(ansiblePlayBookRequest.getAnsibleClientRequest());
                ansibleFileCreationRequestDto.setServerPlaybookPath(AnsibleServiceConstants.DELETE_CHECKOUT_PLAYBOOK_PATH);
                List<String> options = new ArrayList<>();
                options.add("-e");
                options.add("\"");
                options.add("gitCheckoutDir=" + AnsibleServiceConstants.TEMP__GIT_CHECKOUT_PATH);
                options.add("\"");
                ansibleFileCreationRequestDto.setCommandArgs(options);
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_DELETE_CHECKOUT_SERVICE_IMPL_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_DELETE_CHECKOUT_SERVICE_IMPL_ERROR + ex.getMessage());
        }

        return ansibleFileCreationRequestDto;
    }

}
