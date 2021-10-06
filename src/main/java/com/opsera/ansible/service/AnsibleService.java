package com.opsera.ansible.service;

import java.util.Map;

import com.opsera.ansible.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.resources.AnsiblePayloadRequestConfig;
import com.opsera.ansible.util.JobStatus;

/**
 * @author sreeni
 *
 */
public interface AnsibleService {
    
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest);
    
    public AnsiblePlayBookClientRequest getAnsiblePlaybookRequestFromKafka(AnsiblePayloadRequestConfig ansiblePayloadRequestConfig);
    
    public Map<String,JobStatus> getAnsibleJobStatus(Map<String, AnsiblePlayBookResponseDto> ansibleResponse);

}
