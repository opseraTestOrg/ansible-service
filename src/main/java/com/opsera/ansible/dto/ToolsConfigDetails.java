/**
 * 
 */
package com.opsera.ansible.dto;

import lombok.Data;

/**
 * @author sreeni
 *
 */
@Data
public class ToolsConfigDetails {

    private String hostName;
    private String port;
    private String pubKeyPath;
    private String userName;
}
