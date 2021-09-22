/**
 * 
 */
package com.opsera.ansible.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.woostju.ansible.AnsibleClient;
import com.github.woostju.ansible.ReturnValue;
import com.github.woostju.ansible.command.PingCommand;
import com.github.woostju.ansible.command.PlaybookCommand;
import com.opsera.ansible.config.IServiceFactory;
import com.opsera.ansible.exception.AnsibleServiceException;
import com.opsera.ansible.request.dto.AnsibleConnectionClientRequest;
import com.opsera.ansible.request.dto.AnsiblePlayBookClientRequest;
import com.opsera.ansible.request.dto.AnsiblePlayBookResponseDto;
import com.opsera.ansible.resources.AnsibleServiceConstants;
import com.opsera.ansible.service.AnsibleServiceFactory;
import com.opsera.ansible.service.CommandService;
import com.opsera.ansible.util.AnsibleUtility;
import com.opsera.ansible.util.ClientUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author sreeni
 *
 */
@RestController
@Validated
@Api("API to execute different commands to ansible server")
public class CommandController {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommandController.class);

    @Autowired
    private ClientUtility clientUtility;

    @Autowired
    private StopWatch stopwatch;

    @Autowired
    private IServiceFactory serviceFactory;

    @Autowired
    private AnsibleUtility ansibleUtility;

    @Autowired
    CommandService commandService;

    @Autowired
    AnsibleServiceFactory ansibleServiceFactory;

    /**
     * This Api used test the connectivity to Ansible server using Ping Command
     * 
     * @param ansibleClientRequest AnsibleClientRequest
     * @param ansibleHostName      ResponseEntity<Map<String, ReturnValue>>
     */
    @PostMapping(path = "/testConnection")
    @ApiOperation("Test Connectivity with Ansible server")
    public ResponseEntity<Map<String, ReturnValue>> executePingCommand(@RequestBody AnsibleConnectionClientRequest ansibleClientRequest) {
        stopwatch.start();
        Map<String, ReturnValue> result = new HashMap<String, ReturnValue>();
        LOGGER.info(AnsibleServiceConstants.CREATE_NEW_ANSIBLE_CLIENT_IN_COMMAND_OPERATIONS_CONTROLLER_INFO);
        String hostName = null;
        Map<String, ReturnValue> validateErrors = null;
        try {
            validateErrors = commandService.validatePayloadforCommand(ansibleClientRequest);
            if (validateErrors.isEmpty()) {
                hostName = ansibleClientRequest.getHostName();
                AnsibleClient ansibleClient = clientUtility.getClient(ansibleClientRequest);
                ansibleClient.setAnsibleRootPath("");
                result = ansibleClient.execute(new PingCommand(Lists.newArrayList(hostName)), AnsibleServiceConstants.TIMEOUT_SERVER);
                LOGGER.info(AnsibleServiceConstants.PING_ANSIBLE_SERVER_SUCESSFULLY_IN_COMMAND_CONTROLLER_INFO);
                // TODO - Check if the result is empty then create a error response as result
                // empty becomes when there is a issue in connecting to the ansible server.
                // java.lang.RuntimeException: java.lang.RuntimeException: create ssh client
                // fail
                // Error format should be same as the success format.
            } else {
                result = validateErrors;
            }
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_PING_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR, serviceFactory.gson().toJson(ansibleClientRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_PING_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR + ex.getMessage());
        } finally {
            stopwatch.stop();
            LOGGER.info(AnsibleServiceConstants.EXECUTING_PING_COMMAND_COMPLETED, stopwatch.getTotalTimeMillis());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     * Playbook Command
     * 
     * @param ansibleClientRequest
     * @param hostNames
     * @param playBookPath
     * @return
     */
    @PostMapping(path = "/runAPlaybook")
    @ApiOperation("Run playbook in configured servers using playbook command")
    public ResponseEntity<Map<String, AnsiblePlayBookResponseDto>> executePlaybookCommand(@RequestBody AnsibleConnectionClientRequest ansibleClientRequest, String playBookPath) {
        stopwatch.start();
        Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse = new HashMap<>();
        LOGGER.info(AnsibleServiceConstants.EXECUTE_ANSIBLE_PLAYBOOK_IN_ANSIBLE_SERVER_IN_COMMAND_CONTROLLER_INFO);
        try {
            AnsibleClient ansibleClient = clientUtility.getClient(ansibleClientRequest);
            ansibleClient.setAnsibleRootPath("");
            List<String> hosts = new ArrayList<String>();
            hosts.add(ansibleClientRequest.getHostName());
            Map<String, ReturnValue> result = ansibleClient.execute(new PlaybookCommand(hosts, playBookPath, null), AnsibleServiceConstants.TIMEOUT_SERVER);
            ansiblecustomResponse = ansibleUtility.getAnsiblePlaybookResponse(result);
            LOGGER.info(AnsibleServiceConstants.COMPLETED_TO_EXECUTE_PLAYBOOK_IN_ANSIBLE_SERVER_SUCESSFULLY_INFO);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR, serviceFactory.gson().toJson(ansibleClientRequest), playBookPath);
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_THROUGH_ANSIBLE_CLIENT_ERROR + ex.getMessage());
        } finally {
            stopwatch.stop();
            LOGGER.info(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_COMPLETED, stopwatch.getTotalTimeMillis());
        }
        return new ResponseEntity<>(ansiblecustomResponse, HttpStatus.OK);

    }

    /**
     * Playbook Command
     * 
     * @param ansibleClientRequest
     * @param hostNames
     * @param playBookPath
     * @return Map<String, AnsiblePlaybookResponseDto>
     */
    @PostMapping(path = "/runAPlaybookWithArgs")
    @ApiOperation("Run playbook in configured servers using playbook command")
    public ResponseEntity<Map<String, AnsiblePlayBookResponseDto>> executePlaybookCommandWithArguments(@RequestBody AnsiblePlayBookClientRequest ansiblePlayBookRequest) {
        stopwatch.start();
        Map<String, AnsiblePlayBookResponseDto> ansiblecustomResponse = new HashMap<>();
        Map<String, ReturnValue> validateErrors = null;
        LOGGER.info(AnsibleServiceConstants.EXECUTE_ANSIBLE_PLAYBOOK_IN_ANSIBLE_SERVER_IN_COMMAND_CONTROLLER_INFO);
        try {
            validateErrors = commandService.validatePayloadforFileCreation(ansiblePlayBookRequest);
            if (validateErrors.isEmpty()) {
                AnsibleConnectionClientRequest ansibleClientRequest = ansiblePlayBookRequest.getAnsibleClientRequest();
                AnsibleClient ansibleClient = clientUtility.getClient(ansibleClientRequest);
                ansibleClient.setAnsibleRootPath("");
                commandService.downloadFilesFromGithub(ansibleClient, ansiblePlayBookRequest);
                Map<String, ReturnValue> result = commandService.createFile(ansibleClient, ansiblePlayBookRequest);
                commandService.deleteGitCheckoutFiles(ansibleClient, ansiblePlayBookRequest);
                ansiblecustomResponse = ansibleUtility.getAnsiblePlaybookResponse(result);
            } else {
                ansiblecustomResponse = ansibleUtility.getAnsiblePlaybookResponse(validateErrors);
            }
            LOGGER.info(AnsibleServiceConstants.COMPLETED_TO_EXECUTE_PLAYBOOK_IN_ANSIBLE_SERVER_SUCESSFULLY_INFO);
        } catch (Exception ex) {
            LOGGER.error(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_WITH_ARGS_THROUGH_ANSIBLE_CLIENT_ERROR, serviceFactory.gson().toJson(ansiblePlayBookRequest));
            throw new AnsibleServiceException(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_WITH_ARGS_THROUGH_ANSIBLE_CLIENT_ERROR + ex.getMessage());
        } finally {
            stopwatch.stop();
            LOGGER.info(AnsibleServiceConstants.EXECUTING_PLAYBOOK_COMMAND_COMPLETED, stopwatch.getTotalTimeMillis());
        }
        return new ResponseEntity<>(ansiblecustomResponse, HttpStatus.OK);

    }

}
