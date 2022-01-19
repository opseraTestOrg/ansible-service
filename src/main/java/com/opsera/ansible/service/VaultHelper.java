package com.opsera.ansible.service;


import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opsera.ansible.config.AppConfig;
import com.opsera.ansible.config.ServiceFactory;
import com.opsera.ansible.dto.VaultData;
import com.opsera.ansible.dto.VaultRequest;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.resources.AnsibleServiceConstants;


/**
 * Helper for communication with vault
 */
@Service
public class VaultHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(VaultHelper.class);

    @Autowired
    private ServiceFactory serviceFactory;

    @Autowired
    private AppConfig appConfig;

    /**
     * This method used to get the sonar credentials from vault
     *
     * @param customerId
     * @param vaultKey
     * @return
     */
    public String getS3SecretKey(String customerId, String vaultKey) {
        RestTemplate restTemplate = serviceFactory.getRestTemplate();
        String readURL = appConfig.getVaultBaseUrl() + AnsibleServiceConstants.VAULT_READ_ENDPOINT;
        VaultRequest request = VaultRequest.builder().customerId(customerId).componentKeys(Collections.singletonList(vaultKey)).build();

        VaultData response = restTemplate.postForObject(readURL, request, VaultData.class);

        Optional<VaultData> vaultData = Optional.ofNullable(response);
        if (vaultData.isPresent()) {
            return new String(Base64.getDecoder().decode(response.getData().get(vaultKey).getBytes()));
        } else {
            LOGGER.info("Empty response from vault for request: {}", request);
            throw new AnsibleServiceException(String.format("SonarAuthToken Not found in Vault for CustomerID: %s", customerId));
        }
    }

    /**
     * gets the secret from vault
     *
     * @return
     */
    public Map<String, String> getSecrets(String customerId, List<String> secretKeys) {
        RestTemplate restTemplate = serviceFactory.getRestTemplate();
        String readURL = appConfig.getVaultBaseUrl() + AnsibleServiceConstants.VAULT_READ_ENDPOINT;
        VaultRequest request = VaultRequest.builder().customerId(customerId).componentKeys(secretKeys).build();
        VaultData response = restTemplate.postForObject(readURL, request, VaultData.class);
        Optional<VaultData> vaultData = Optional.ofNullable(response);
        if (vaultData.isPresent()) {
            Map<String, String> vaultDataMap = new LinkedHashMap<>();
            vaultData.get().getData().entrySet().forEach(data -> vaultDataMap.put(data.getKey(), new String(Base64.getDecoder().decode(data.getValue().getBytes()))));
            return vaultDataMap;
        } else {
            throw new AnsibleServiceException(String.format("AnsibleDetails Not found in Vault for CustomerID: %s", customerId));
        }
    }

}
