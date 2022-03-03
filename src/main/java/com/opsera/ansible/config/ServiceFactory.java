/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.opsera.ansible.service.CommandService;
import com.opsera.ansible.service.VaultHelper;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author sreeni
 *
 */
@Getter
@Component
public class ServiceFactory {

    @Autowired
    @Accessors(fluent = true)
    private Gson gson;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommandService commandService;

    @Autowired
    private VaultHelper vaultHelper;

}
