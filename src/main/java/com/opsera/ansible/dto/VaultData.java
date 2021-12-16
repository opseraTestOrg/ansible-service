package com.opsera.ansible.dto;

import lombok.Data;

import java.util.Map;

@Data
public class VaultData {
    Map<String, String> data;
}
