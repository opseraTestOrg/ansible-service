package com.opsera.ansible.client.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opsera.ansible.client.command.Command;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.resources.AnsibleClientConstants;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.ssh.SshClientConfig;
import com.opsera.ansible.ssh.SshResponse;
import com.opsera.ansible.ssh.exception.SshException;
import com.opsera.ansible.ssh.pool.SshClientPoolConfig;
import com.opsera.ansible.ssh.pool.SshClientWrapper;
import com.opsera.ansible.ssh.pool.SshClientsPool;
import com.opsera.ansible.util.SystemCommandExecutor;

/**
 * 
 * ansible client, execute ansible adhoc command on remote ansible server, or
 * execute on local server
 * 
 * @author sreeni
 * 
 * 
 */

public class AnsibleClient {

    

    private final static Logger logger = LoggerFactory.getLogger(AnsibleClient.class);

    private SshClientsPool sshClientsPool;

    /**
     * Ansible client to local server
     */
    public AnsibleClient() {

    }

    /**
     * Ansible client for remote server
     * 
     * @param config ssh config of remote machine
     */
    public AnsibleClient(SshClientConfig config) {
        this.hostSshConfig = config;
    }

    /**
     * Ansible client for remote server
     * 
     * @param config         ssh config of remote machine
     * @param sshClientsPool the pool used to ssh client to remote server
     */
    public AnsibleClient(SshClientConfig config, SshClientsPool sshClientsPool) {
        this.hostSshConfig = config;
        this.sshClientsPool = sshClientsPool;
    }

    private SshClientConfig hostSshConfig;

    private String ansibleRootPath = AnsibleClientConstants.ANSIBLE_ROOT_PATH;

    private String inventoryPath = AnsibleClientConstants.ETC_ANSIBLE_HOSTS_PATH;

    /**
     * set custom Ansible inventory, default is /etc/ansible/hosts
     * 
     * @param inventoryPath custom Ansible inventory
     * @return AnsibleClient
     */
    public AnsibleClient setInventoryPath(String inventoryPath) {
        this.inventoryPath = inventoryPath;
        return this;
    }

    /**
     * set Ansible executables root folder, the folder should contain executable
     * ansible | ansible-playbook | ansible-inventory .etc, default is /usr/bin/
     * 
     * @param ansibleRootPath the root folder of Ansible executables
     * @return AnsibleClient
     */
    public AnsibleClient setAnsibleRootPath(String ansibleRootPath) {
        this.ansibleRootPath = ansibleRootPath;
        return this;
    }

    /**
     * 
     * @return Ansible executables root folder
     */
    public String getAnsibleRootPath() {
        return ansibleRootPath;
    }

    /**
     * 
     * @return Ansible inventory path
     */
    public String getInventoryPath() {
        return inventoryPath;
    }

    /**
     * 
     * @return host ssh config of ansible server
     */
    public SshClientConfig getHostSshConfig() {
        return hostSshConfig;
    }

    /**
     * execute Ansible command
     * 
     * @param command          the command to be executed
     * @param timeoutInSeconds timeout in seconds
     * @return return value of executed command, key:ip address, value:
     *         {@link ReturnValue}
     */
    public Map<String, ReturnValue> execute(Command command, int timeoutInSeconds) {
        List<String> commands = command.createAnsibleCommands(this, command);
        logger.info("execute commands " + commands + " in " + timeoutInSeconds);
        Exception exception = null;
        Map<String, ReturnValue> responses = new HashMap<>();
        if (this.hostSshConfig == null) {
            // execute command locally
            try {
                List<String> stdout = SystemCommandExecutor.newExecutor().executeCommand(commands, timeoutInSeconds);
                responses = command.parseCommandReturnValues(stdout);
            } catch (Exception e) {
                exception = e;
            }
        } else {
            // execute command remotely
            String commandStr = commands.stream().collect(Collectors.joining(" "));
            try {
                SshResponse sshResponse;
                if (this.sshClientsPool == null) {
                    // create ssh client directly
                    SshClientWrapper wrapper = new SshClientWrapper(this.hostSshConfig, new SshClientPoolConfig());
                    try {
                        wrapper.connect(timeoutInSeconds).auth().startSession();
                        sshResponse = wrapper.executeCommand(commandStr, timeoutInSeconds);
                    } catch (SshException ex) {
                        logger.error(AnsibleServiceConstants.ERROR_WHILE_CONNECTING_ANSIBLE_SERVER_ERROR, commandStr);
                        throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_PING_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR + ex.getMessage());

                    } finally {
                        wrapper.disconnect();
                    }
                } else {
                    // borrow ssh client from pool
                    SshClientWrapper client = this.sshClientsPool.client(this.hostSshConfig);
                    sshResponse = client.executeCommand(commandStr, timeoutInSeconds);
                }
                if (sshResponse.getCode() == 0) {
                    try {
                        responses = command.parseCommandReturnValues(sshResponse.getStdout());
                    } catch (Exception e) {
                        logger.error(AnsibleClientConstants.PARSE_ANSIBLE_OUTPUT_FAIL_ERROR, e);
                    }
                }
                exception = sshResponse.getException();
            } catch (Exception e) {
                logger.error(AnsibleClientConstants.EXEC_COMMAND_FAIL_ERROR, e);
                exception = e;
            }
        }
        for (String ip : command.getHosts()) {
            if (!responses.containsKey(ip)) {
                ReturnValue response = new ReturnValue();
                response.setResult(ReturnValue.Result.failed);
                if (exception != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    exception.printStackTrace(pw);
                    response.setStdout(Lists.newArrayList(sw.toString()));
                }
            }
        }
        return responses;
    }

}
