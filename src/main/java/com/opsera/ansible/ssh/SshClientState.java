package com.opsera.ansible.ssh;



/**
 * 
 * state of SshClient, See {@link SshClient#getState()} for more information
 * 
 * @author sreeni
 *
 */
public enum SshClientState {
        inited, 
        connected, 
        disconnected
}
