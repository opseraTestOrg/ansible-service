package com.opsera.ansible.service;


import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opsera.ansible.dto.VaultRequest;
import com.opsera.ansible.exception.AnsibleServiceException;


/**
 * Helper for communication with vault
 */
@Service
public class VaultService {

    public static final Logger LOGGER = LoggerFactory.getLogger(VaultService.class);

    @Autowired
    private com.opsera.core.helper.VaultHelper vaultHelper;

    /**
     * This method used to get the sonar credentials from vault
     *
     * @param customerId
     * @param vaultKey
     * @return
     */
    public String getS3SecretKey(String customerId, String vaultKey) {
        VaultRequest request = VaultRequest.builder().customerId(customerId).componentKeys(Collections.singletonList(vaultKey)).build();
        Map<String, String> secrets = vaultHelper.getSecrets(customerId, Collections.singletonList(vaultKey));
        if (!secrets.isEmpty()) {
            return new String(Base64.getDecoder().decode(secrets.get(vaultKey).getBytes()));
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
        Map<String, String> secrets = vaultHelper.getSecrets(customerId, secretKeys);
        if (!secrets.isEmpty()) {
            Map<String, String> vaultDataMap = new LinkedHashMap<>();
            secrets.entrySet().forEach(data -> vaultDataMap.put(data.getKey(), new String(Base64.getDecoder().decode(data.getValue().getBytes()))));
            return vaultDataMap;
        } else {
            throw new AnsibleServiceException(String.format("AnsibleDetails Not found in Vault for CustomerID: %s", customerId));
        }
    }

}
