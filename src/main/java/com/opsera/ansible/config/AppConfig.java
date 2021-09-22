/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.Getter;

/**
 * @author sreeni
 *
 */
@Component
@Configuration
@Getter
public class AppConfig {

    /**
     * Creates a prototype bean for stop watch bean
     * 
     * 
     * @return
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public StopWatch stopWatch() {
        return new StopWatch();
    }

    /**
     * Service factory bean creation. Bean factory will be created with the
     * interface, spring will take care of maintaining the bean lifecycle.
     * 
     * @return
     */
    @Bean
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(IServiceFactory.class);
        return factoryBean;
    }

}
