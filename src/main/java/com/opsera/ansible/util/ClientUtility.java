/**
 * 
 */
package com.opsera.ansible.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opsera.ansible.client.util.AnsibleClient;
import com.opsera.ansible.config.ServiceFactory;
import com.opsera.ansible.dto.AnsibleConnectionClientRequest;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.ssh.SshClientConfig;
import com.opsera.ansible.ssh.pool.SshClientsPool;


/**
 * @author sreeni
 *
 */
@Component
public class ClientUtility {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClientUtility.class);

    @Autowired
    SshClientsPool pool;

    @Autowired
    private ServiceFactory serviceFactory;

    /**
     * @param ansibleClientRequest AnsibleClientRequest
     * @return ansibleClient AnsibleClient
     */
    public AnsibleClient getClient(AnsibleConnectionClientRequest ansibleClientRequest) {
        AnsibleClient ansibleClient = null;

        LOGGER.info(AnsibleServiceConstants.CREATE_NEW_ANSIBLE_CLIENT_USING_ANSIBLE_CLIENT_REQUEST_INFO, serviceFactory.gson().toJson(ansibleClientRequest));
        //ansibleClientRequest.getPubKeyPath())
        //AnsibleClientConstants.PUB_KEY
        try {
            ansibleClient = new AnsibleClient(new SshClientConfig(ansibleClientRequest.getHostName(), ansibleClientRequest.getPort(), ansibleClientRequest.getUserName(),
                    ansibleClientRequest.getPassword(),ansibleClientRequest.getPubKeyPath()), pool);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_CREATING_ANSIBLE_CLIENT_MSG_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTION_FAILED_WHILE_CREATING_ANSIBLE_CLIENT_ERROR + ex.getMessage());

        }
        LOGGER.info(AnsibleServiceConstants.COMPLETED_TO_CREATE_NEW_ANSIBLE_CLIENT_USING_ANSIBLE_CLIENT_REQUEST_INFO);
        return ansibleClient;

    }
    
    
    

}
