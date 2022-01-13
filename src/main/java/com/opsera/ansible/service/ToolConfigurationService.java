package com.opsera.ansible.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.opsera.ansible.config.AppConfig;
import com.opsera.ansible.config.IServiceFactory;
import com.opsera.ansible.dto.ToolsConfigurations;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.resources.AnsibleServiceConstants;

/**
 * The Class ToolConfigurationService.
 * 
 * @author sreeni
 *
 */
@Service
public class ToolConfigurationService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ToolConfigurationService.class);

    /** The app config. */
    @Autowired
    private AppConfig appConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IServiceFactory serviceFactory;

    /**
     * Making a Rest call to Mongo Database to get the tool Configuration Details
     * queries pipeline-config services and fetches the aws related configurations
     *
     * @param jiraToolId
     * @return
     */
    public ToolsConfigurations getToolConfigurationDetails(String toolConfigId, String customerId) {
        ToolsConfigurations configurations = new ToolsConfigurations();
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getPipelineConfigBaseUrl() + AnsibleServiceConstants.V2_REGISTRY_TOOL_END_POINT)
                    .queryParam(AnsibleServiceConstants.QUERY_PARAM_TOOL_ID, toolConfigId).queryParam(AnsibleServiceConstants.QUERY_PARAM_CUSTOMER_ID, customerId);
            configurations = restTemplate.getForObject(uriBuilder.toUriString(), ToolsConfigurations.class);
        } catch (RestClientException ex) {
            LOGGER.error(AnsibleServiceConstants.ERROR_WHILE_RETRIVING_TOOL_CONFIG_DETAILS_FROM_ENDPOINT_MSG_ERROR, toolConfigId, customerId);
            throw new AnsibleServiceException(AnsibleServiceConstants.ERROR_WHILE_RETRIVING_TOOL_CONFIG_DETAILS_FROM_ENDPOINT_ERROR + ex.getMessage());
        }
        return configurations;
    }

}
