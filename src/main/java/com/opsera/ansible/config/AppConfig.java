/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

/**
 * @author sreeni
 *
 */
@Component
@Configuration
@Getter
public class AppConfig {

    @Value("${vault.config.baseurl}")
    private String vaultBaseUrl;

    @Value("${kafka.config.baseurl}")
    private String kafkaBaseUrl;

    @Value("${pipeline.config.baseurl}")
    private String pipelineConfigBaseUrl;

    /**
     * 
     * create rest template bean
     * 
     * @return
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
