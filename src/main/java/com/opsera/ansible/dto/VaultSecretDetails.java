/**
 * 
 */
package com.opsera.ansible.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class VaultSecretDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3522362464794422602L;
    private String name;
    private String vaultKey;

}
