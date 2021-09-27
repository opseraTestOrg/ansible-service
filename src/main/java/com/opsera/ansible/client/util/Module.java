package com.opsera.ansible.client.util;

/**
 * 
 * ansible module type
 * 
 * @author sreeni
 */
public enum Module {
        script, 
        command, 
        ping, 
        copy, 
        playbook, 
        git, 
        ansible_inventory, 
        file, 
        get_url, 
        setup
}
