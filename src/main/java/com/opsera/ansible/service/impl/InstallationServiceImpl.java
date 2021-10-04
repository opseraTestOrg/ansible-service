package com.opsera.ansible.service.impl;

import java.util.Map;

import com.opsera.ansible.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.resources.AnsiblePayloadRequestConfig;
import com.opsera.ansible.service.AnsibleService;
import com.opsera.ansible.util.JobStatus;

/**
 * @author sreeni
 *
 */
public class InstallationServiceImpl implements AnsibleService {

    @Override
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest) {
        //
        // String version=ansibleMap.getValue("Version"))
        // options List: return Playbook Reqeust
        return new AnsiblePlaybookServerRequestDto();
    }

    @Override
    public AnsiblePlayBookClientRequest getAnsiblePlaybookRequestFromKafka(AnsiblePayloadRequestConfig ansiblePayloadRequestConfig) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, JobStatus> getAnsibleJobStatus(Map<String, AnsiblePlayBookResponseDto> ansibleResponse) {
        // TODO Auto-generated method stub
        return null;
    }

}
