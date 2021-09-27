package com.opsera.ansible.ssh;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Configuration used by {@link SshClient} to connect to remote server instance
 * 
 * @author sreeni
 * 
 */
@Data
@NoArgsConstructor
public class SshClientConfig {
    private String host;
    private int port;
    private String username;
    private String password;
    private String privateKeyPath;
    private String id;

    /**
     * 
     * @param host           server host address
     * @param port           server ssh port
     * @param username       server ssh username
     * @param password       server ssh password
     * @param privateKeyPath local security key used to connect to server
     */
    public SshClientConfig(String host, int port, String username, String password, String privateKeyPath) {
        this.id = host + port + username;
        if (null != password && password.length() > 0) {
            this.id += password;
        }
        if (privateKeyPath != null) {
            this.id += privateKeyPath;
        }
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.privateKeyPath = privateKeyPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SshClientConfig) {
            return id.equals(((SshClientConfig) obj).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return this.id;
    }
}
