package com.opsera.ansible.service;

import com.opsera.ansible.request.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.request.dto.AnsiblePlayBookClientRequest;

/**
 * @author sreeni
 *
 */
public interface AnsibleService {
    
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest);

}
