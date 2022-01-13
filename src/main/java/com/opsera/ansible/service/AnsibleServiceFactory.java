package com.opsera.ansible.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.impl.DeleteCheckoutFolderServiceImpl;
import com.opsera.ansible.service.impl.DownloadGitServiceImpl;
import com.opsera.ansible.service.impl.GenericPlaybookServiceImpl;

/**
 * @author sreeni
 *
 */
@Component
public class AnsibleServiceFactory {

    public static final Logger LOGGER = LoggerFactory.getLogger(AnsibleServiceFactory.class);

    public enum AnsibleServiceType {
         DownloadFromGit, DeleteGitCheckoutFolder, ExecutePlaybook
    }

    /**
     * @param AnsibleServiceType serviceType
     * @return AnsibleService ansibleService
     */
    public AnsibleService getAnsibleService(AnsibleServiceType serviceType) {
        try {
            if (serviceType == AnsibleServiceType.DownloadFromGit) {
                return new DownloadGitServiceImpl();
            } else if (serviceType == AnsibleServiceType.DeleteGitCheckoutFolder) {
                return new DeleteCheckoutFolderServiceImpl();
            }else {
                return new GenericPlaybookServiceImpl();
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_GETTING_ANSIBLE_SERVICE_IMPL_MSG_ERROR, serviceType);
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_GETTING_ANSIBLE_SERVICE_IMPL_ERROR + ex.getMessage());
        }
    }
}
