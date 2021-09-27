package com.opsera.ansible.ssh.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opsera.ansible.ssh.pool.SshClientPoolConfig;
import com.opsera.ansible.ssh.pool.SshClientsPool;



/**
 * @author sreeni
 *
 */
@Configuration
@EnableConfigurationProperties(SshClientPoolProperties.class)
public class SshClientPoolAutoConfiguration {
        
        private final SshClientPoolProperties properties;
        
        public SshClientPoolAutoConfiguration(SshClientPoolProperties properties) {
                this.properties = properties;
        }
        
        /**
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(SshClientsPool.class)
        SshClientsPool sshClientsPool() {
                return new SshClientsPool(sshClientPoolConfig());
        }
        
        /**
         * @return
         */
        SshClientPoolConfig sshClientPoolConfig() {
                SshClientPoolConfig poolConfig = new SshClientPoolConfig(properties.getMaxActive()
                                ,properties.getMaxIdle()
                                ,properties.getIdleTime()
                                ,properties.getMaxWait());
                if(properties.getSshj()!=null) {
                        poolConfig.setServerCommandPromotRegex(properties.getSshj().getServerCommandPromotRegex());
                }
                if (properties.getSshClientImplClass()!=null) {
                        try {
                                poolConfig.setSshClientImplClass(Class.forName(properties.getSshClientImplClass()));
                        } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                        }
                }
                return poolConfig;
        }
}
