package com.opsera.ansible.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author sreeni
 *
 */
@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class AnsibleConnectionClientRequest {
    
    private String  hostName;
    
    private int port;
    
    private String userName;
    
    private String password;
    
    private String pubKeyPath;

}
