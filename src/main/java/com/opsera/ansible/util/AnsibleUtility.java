package com.opsera.ansible.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.github.woostju.ansible.ReturnValue;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.request.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.resources.AnsibleServiceConstants;

/**
 * @author sreeni
 *
 */
@Component
public class AnsibleUtility {

    /**
     * 
     * @param ansibleResponse Map<String, ReturnValue>
     * @return Map<String, AnsiblePlaybookResponseDto>
     */
    public Map<String, AnsiblePlayBookResponseDto> getAnsiblePlaybookResponse(Map<String, ReturnValue> ansibleResponse) {
        Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse = new HashMap<>();
        try {
            if (ansibleResponse != null) {
                ansibleResponse.entrySet().parallelStream().filter(Objects::nonNull).forEach(ansibleResInstance -> {
                    String stautsForServer = ansibleResInstance.getKey();
                    Object valueRef = ansibleResInstance.getValue();
                    if (valueRef instanceof ReturnValue) {
                        AnsiblePlayBookResponseDto ansibleCommandLineResp = new AnsiblePlayBookResponseDto();
                        ansibleCommandLineResp.setRc(((ReturnValue) valueRef).getRc());
                        ansibleCommandLineResp.setResult(((ReturnValue) valueRef).getResult().toString());
                        ansibleCommandLineResp.setSuccess(((ReturnValue) valueRef).isSuccess());
                        ansibleCommandLineResp.setStdout(((ReturnValue) valueRef).getStdout());
                        ansiblecustomResponse.put(stautsForServer, ansibleCommandLineResp);
                    } else if (valueRef instanceof Map<?, ?>) {
                        if (!((Map<?, ?>) valueRef).isEmpty()) {
                            AnsiblePlayBookResponseDto ansibleCommandLineResp = new AnsiblePlayBookResponseDto();
                            ansibleCommandLineResp.setRc((int) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RC));
                            ansibleCommandLineResp.setResult((String) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_RESULT));
                            ansibleCommandLineResp.setSuccess((Boolean) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_SUCCESS));
                            ansibleCommandLineResp.setStdout((List<String>) ((Map<?, ?>) valueRef).get(AnsibleServiceConstants.RESPONSE_STDOUT));
                            ansiblecustomResponse.put(stautsForServer, ansibleCommandLineResp);
                        }
                    }

                });
            }
        } catch (Exception ex) {
            throw new AnsibleServiceException(AnsibleServiceConstants.PARSING_PLAY_BOOK_RESPONSE_FROM_ANSIBLE_ERROR + ex.getMessage());
        }
        return ansiblecustomResponse;

    }

}
