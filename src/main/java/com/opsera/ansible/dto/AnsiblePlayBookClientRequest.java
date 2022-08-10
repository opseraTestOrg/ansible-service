package com.opsera.ansible.dto;

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
public class AnsiblePlayBookClientRequest {

    private AnsibleConnectionClientRequest ansibleClientRequest;

   // private Map<String, String> commandArgs;

   // private AnsibleServiceType serviceType;

    private String gitFileName;

    private String gitFileLocation;

}
