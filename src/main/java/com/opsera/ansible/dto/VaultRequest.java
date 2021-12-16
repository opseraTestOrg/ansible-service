package com.opsera.ansible.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VaultRequest {

    private String customerId;

    private List<String > componentKeys;
}
