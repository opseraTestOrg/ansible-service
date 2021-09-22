package com.opsera.ansible.service.impl;

import com.opsera.ansible.request.dto.AnsiblePlaybookServerRequestDto;
import com.opsera.ansible.request.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.service.AnsibleService;

/**
 * @author sreeni
 *
 */
public class InstallationServiceImpl implements AnsibleService {

    @Override
    public AnsiblePlaybookServerRequestDto getAnsiblePlaybookRequest(AnsiblePlayBookClientRequest ansiblePlayBookRequest) {
        //
        // String version=ansibleMap.getValue("Version"))
        //options List: return Playbook Reqeust
        return new AnsiblePlaybookServerRequestDto();
    }

}
