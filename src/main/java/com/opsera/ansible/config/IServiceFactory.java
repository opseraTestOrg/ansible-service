/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.opsera.ansible.service.CommandService;

/**
 * @author sreeni
 *
 */
@Component
public interface IServiceFactory {

    public Gson gson();

    public StopWatch stopWatch();

    public RestTemplate getRestTemplate();

    public CommandService getCommandService();

}
