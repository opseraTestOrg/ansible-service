/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.opsera.ansible.service.CommandService;
import com.opsera.ansible.service.VaultService;

/**
 * @author sreeni
 *
 */
@Component
public interface ServiceFactory {

    public Gson gson();

    public CommandService getCommandService();

    public VaultService getVaultHelper();

}
