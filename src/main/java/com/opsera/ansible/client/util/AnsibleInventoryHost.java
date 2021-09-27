package com.opsera.ansible.client.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class AnsibleInventoryHost {
        
        private String host_name;
        
        private String ansible_port;
        
        private String ansible_user;
        
        private String ansible_ssh_private_key_file;
        
        private String ansible_ssh_pass;
        
        private String ansible_become_password;
        
        private String ansible_become;
}
