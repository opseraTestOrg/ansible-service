/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * @author sreeni
 *
 */
@Component
@Configuration
@Getter
public class AppConfig {

    @Value("${pipeline.config.baseurl}")
    private String pipelineConfigBaseUrl;

    /**
     * Service factory for Spring to inject
     * 
     * 
     * @return
     */
    @Bean
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(ServiceFactory.class);
        return factoryBean;
    }

}
