package com.opsera.ansible.request.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sreeni
 *
 */
@Data
@NoArgsConstructor
public class AnsiblePlayBookResponseDto {

    private String result;

    private int rc;

    private Boolean success;
    
    private List<String> stdout; 

}
