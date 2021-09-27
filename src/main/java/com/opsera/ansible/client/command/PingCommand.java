package com.opsera.ansible.client.command;

import java.util.List;

import com.opsera.ansible.client.util.Module;

/**
 * Try to connect to host, verify a usable python and return pong on success
 * <p>
 * Get more information from <a href=
 * "https://docs.ansible.com/ansible/latest/modules/ping_module.html">ping
 * module</a>.
 * 
 * @author sreeni
 *
 */
public class PingCommand extends Command {

    /**
     * 
     * @param hosts target hosts
     */
    public PingCommand(List<String> hosts) {
        this(hosts, null, null);
    }

    public PingCommand(List<String> hosts, List<String> moduleArgs, List<String> options) {
        super(hosts, Module.ping.toString(), moduleArgs, options);
    }
}
