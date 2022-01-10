/**
 * 
 */
package com.opsera.ansible.resources;

import java.util.Map;

import lombok.Data;

/**
 * @author sreeni
 *
 */
@Data
public class AnsiblePayloadRequestConfig {

    private String customerId;
    private String pipelineId;
    private String stepId;
    private int runCount;
    private String action;
    private String toolConfigId;
//    private Map<String, String> commandArgs;
    private String gitFileLocation;
    private String gitFileName;
//    private String serviceType;

}
