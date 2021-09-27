package com.opsera.ansible.ssh.pool;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opsera.ansible.ssh.SshClient;
import com.opsera.ansible.ssh.SshClientConfig;
import com.opsera.ansible.ssh.SshClientEventListener;
import com.opsera.ansible.ssh.SshClientFactory;
import com.opsera.ansible.ssh.SshClientState;
import com.opsera.ansible.ssh.SshResponse;
import com.opsera.ansible.ssh.exception.SshException;



/**
 * A wrapper class of {@link SshClient} used by {@link SshClientsPool}
 * 
 * @author sreeni
 *
 */
public class SshClientWrapper implements SshClientEventListener{
        
        private final static Logger logger = LoggerFactory.getLogger(SshClientWrapper.class);
        
        private String id;
        
        private SshClient client;
        
        SshClientEventListener listener;
        
        SshClientConfig config;
        
        public String getId() {
                return this.id;
        }
        
        public void setListener(SshClientEventListener listener) {
                this.listener = listener;
        }
        
        public SshClientConfig getConfig() {
                return this.config;
        }
        
        public SshClientWrapper(SshClientConfig config, SshClientPoolConfig poolConfig) {
                this.id = UUID.randomUUID().toString();
                this.config = config;
                this.client = SshClientFactory.newInstance(config, poolConfig);
        }
        
        public SshClientWrapper setEventListener(SshClientEventListener listener) {
                this.listener = listener;
                this.client.setEventListener(this);
                return this;
        }
        
        /**
         * @param timeoutInSeconds
         * @return
         * @throws SshException
         */
        public SshClientWrapper connect(int timeoutInSeconds) throws SshException {
                client.connect(timeoutInSeconds);
                return this;
        }

        /**
         * @return
         * @throws SshException
         */
        public SshClientWrapper auth() throws SshException{
                if(null!=this.config.getPassword() && this.config.getPassword().length()>0) {
                        client.authPassword();
                }else if(null!=this.config.getPrivateKeyPath() && this.config.getPrivateKeyPath().length()>0) {
                        client.authPublickey();
                }else {
                        client.authPublickey();
                }
                return this;
        }
        

        /**
         * @return
         * @throws SshException
         */
        public SshClientWrapper startSession() throws SshException{
                client.startSession(true);
                return this;
        }

        
        /**
         * @param command
         * @param timeoutInSeconds
         * @return
         */
        public SshResponse executeCommand(String command, int timeoutInSeconds){
                SshResponse response = client.executeCommand(command, timeoutInSeconds);
                return response;
        }

        /**
         * 
         */
        public void disconnect() {
                client.disconnect();
        }
        
        public boolean equals(Object obj) {  
                if(obj instanceof SshClientWrapper){
                        return id.equals(((SshClientWrapper)obj).getId());
                }
                return false;
        }  
        
        public int hashCode(){
                return id.hashCode();
        }
        
        public SshClientState getState() {
                return client.getState();
        }

        public String toString() {
                return "["+this.id+"|"
                                        +this.config.getHost()+"|"
                                        +this.config.getPort()+"|"
                                        +this.getState()+"]";
        }

        @Override
        public void didExecuteCommand(Object client) {
                this.listener.didExecuteCommand(this);
        }

        @Override
        public void didDisConnected(Object client) {
                this.listener.didDisConnected(this);
        }

        @Override
        public void didConnected(Object client) {
                this.listener.didConnected(this);
        }
        
}
