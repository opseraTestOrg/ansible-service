package com.opsera.ansible.resources;

import java.util.regex.Pattern;

public class AnsibleClientConstants {

    public static Pattern play_recap_pattern = Pattern.compile("PLAY RECAP (\\*)*");
    public static Pattern unreachable_pattern = Pattern.compile("unreachable=\\d");
    public static Pattern failed_pattern = Pattern.compile("failed=\\d");
    public static Pattern changed_pattern = Pattern.compile("changed=\\d");
    public static Pattern ok_pattern = Pattern.compile("ok=\\d");
    public static Pattern digital_pattern = Pattern.compile("\\d");
    public static final String ANSIBLE_ROOT_PATH = "/usr/bin/";
    public static final String ETC_ANSIBLE_HOSTS_PATH = "/etc/ansible/hosts";
    public static final String COMMAND_PROMPT_REGEX_STRING = "[\\[]?.+@.+~[\\]]?[#\\$] *";

    // 42.159.4.34 | CHANGED | rc=0 >>
    public static Pattern head_type1_pattern = Pattern.compile("^\\d+\\.\\d+\\.\\d+\\.\\d+( \\| ).*( \\| ).*>>$");
    public static Pattern head_type1_rc_pattern = Pattern.compile("rc=\\d");
    public static Pattern head_type1_result_pattern = Pattern.compile("( \\| ).*( \\| )");
    // 42.11.11.11 | UNREACHABLE! => {
    public static Pattern head_type2_pattern = Pattern.compile("^\\d+\\.\\d+\\.\\d+\\.\\d+( \\| ).+=>.*\\{$");
    public static Pattern head_type2_result_pattern = Pattern.compile("( \\| ).*( =>)");
    // the host is not in inventory
    public static Pattern head_inventory_no_host_pattern = Pattern.compile(".*(Could not match supplied host pattern).*");
    public static Pattern head_ip_pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");

    public static final String RESULT_FAILED = "FAILED!";
    public static final String RESULT_UNREACHABLE = "UNREACHABLE!";
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_CHANGED = "CHANGED";

    public static final String ANSIBLE_PLAYBOOK_MSG = "ansible-playbook";
    public static final String ANSIBLE_MSG = "ansible";

    public static final String WARNING_COULD_NOT_MATCH_SUPPLIED_HOST_PATTERN_IGNORING = "[WARNING]: Could not match supplied host pattern, ignoring:";
    public static final String EXEC_COMMAND_FAIL_ERROR = "exec command fail.";
    public static final String PARSE_ANSIBLE_OUTPUT_FAIL_ERROR = "parse ansible output fail";
    public static final String CLOSE_IO_ERROR = "close IO error";
    public static final String START_SESSION_FAIL_ERROR = "start session fail";
    public static final String START_SESSION_FAIL_WITH_SERVER_INPUT_ERROR = "start session fail with server input:";
    public static final String AUTH_WITH_PASSWORD_DEBUG = "auth with password";
    public static final String SSHJ_GET_EXCEPTION_WHEN_CONNECT_ERROR = "sshj get exception when connect and will retry one more time ";

    public static final String MISSING_CLIENT_CONFIG_ERROR = "missing client config";
    public static final String EXECUTE_SSH_COMMAND_TIMEOUT = "execute ssh command timeout";
    public static final String EXECUTE_SSH_COMMAND_ERROR = "execute ssh command error ";
    public static final String CLOSE_SSH_CONENCTION_ERROR = "close ssh conenction error";
    public static final String CLOSE_SESION_ERROR = "close sesion error";
    public static final String CLOSE_SSH_SHELL_ERROR = "close ssh shell error";
    public static final String SEND_CTRL_C_COMMAND_FAIL_ERROR = "send ctrl+c command fail";
    public static final String SEND_CTR_C_COMMAND_SUCCESS_DEBUG = "send ctr-c command success ";
    public static final String SEND_CTR_C_COMMAND_DEBUG = "send ctr-c command ... ";
    public static final String TIMEOUT_MSG = "timeout";
    public static final String EXECUTE_COMMAND_TIMEOUT_ERROR = "execute command timeout";
    public static final String LOST_CONNECTION_EEROR = "lost connection";
    public static final String EXECUTE_COMMAND_FAIL_ERROR = "execute command fail";
    public static final String COMMAND_EXECUTE_SUCCESS_WITH_RAW_OUTPUT_INFO = "command execute success with raw output";
    public static final String CLIENT_NOT_CONNECTED = "client not connected";

    public static final String ERROR_EXECUTE_SYSTEM_COMMAND = "error execute system command";

}
