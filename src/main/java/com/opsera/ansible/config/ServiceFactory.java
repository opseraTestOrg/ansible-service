/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.opsera.ansible.service.CommandService;
import com.opsera.ansible.service.VaultService;

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
    private CommandService commandService;

    @Autowired
    private VaultService vaultHelper;

}
