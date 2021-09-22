package com.opsera.ansible.request.dto;

import java.util.List;

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
public class AnsiblePlaybookServerRequestDto {
    
    private AnsibleConnectionClientRequest ansibleClientRequest;

    private List<String> commandArgs;
    
    private String serverPlaybookPath;

  

}
