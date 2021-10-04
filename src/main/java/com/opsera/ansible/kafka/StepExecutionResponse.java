package com.opsera.ansible.kafka;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepExecutionResponse {

    private String pipelineId;

    private String customerId;

    private String stepId;

    private String tool;

    private Integer runCount;

    private Map<String, Object> additionalArgs;

    private String status;

    private String eventType;

    private String gitTaskId;

    private String type;

    private String messsage;

    private String message;
}
