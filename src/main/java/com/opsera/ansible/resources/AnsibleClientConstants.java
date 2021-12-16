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
    
    public static final String PUB_KEY = "-----BEGIN RSA PRIVATE KEY-----\r\n"
            + "MIIEowIBAAKCAQEA1Zp/rbzbgMefCgpZ3etAch9QYBLfv/GwLshCbn5KDxt5uihNXB+fTH4eVvbx\r\n"
            + "+7JGgaF+J9PT6HVYJQlwxH/+G0dccEc3EpP2GttYafoAqdljDAd08WBkXQk2piAdhDurEk6zyur6\r\n"
            + "C4GYLIk5gjYZuMC6Zq16yxbs4E83YT/TQdFsiS+0JhIMQ3trjCN2juyGWusieVCCCA/dH0tAcN8i\r\n"
            + "+uvWZemBjQ4YgDDcp6+Dw7+dcDiocEknSmq5C/t0rHFbgjJzHkjSMq9/JGneoEgLwjB7BhQA7i6b\r\n"
            + "63UXRjGHXSpvqyy6zu5yxz1F2phMWx/I8CBtE+dHLsj8QZHHsE4T7QIDAQABAoIBAQCW0r+iqn6H\r\n"
            + "QOygR0zLMKhRrKCxJcKNopdXgRm/MTcT0b+y3XA6Qy3U8hx0JWwZ3/h+4cvVTXIMaobxp+fTJ9kB\r\n"
            + "4/J2aSKO4KDet9CHC/rZ82BHhyBgq0JSmwXWaUjOLN+PTrkO57nk0/3iPIa51X91/8feZlIVArwB\r\n"
            + "ao3dAhcmGfIYs4somzdUJLWkPF3Lzyl5+QrrY2sIKTX67vmTRm+9MnBDPe+t6fRlTIPCfMUSV8ps\r\n"
            + "82/nbUSMRBfJqYQ5bbpVUuEQ0hSfje7/fIuBDGa8qq9suzs5rLdvD6axxiLHuW1mENXk57DtSFIy\r\n"
            + "x9kjmx7qyIJTM+B2ApWsNXIhWt0hAoGBAPW4tGJdxH538jFHzOLmPktxClJGcink4m7BY/+OZSe7\r\n"
            + "3Tt/SvG6p5h73byukmr8ToA0IWu28fF/PRFBOxCPggRMCOTAEhoXqwD8RpGM5ZgtiNlG0fhC0UwW\r\n"
            + "Q4sPpfdpTfAcHlLw3deRq3He8cYDq8oMvE3ZqmFM0TSGRSSxkVvVAoGBAN6J3D+672nqyAkFJPse\r\n"
            + "v3M65itCzgWqoXS9MdzYc8H38Lqc5ZyQMCw1gvHGLorGGSpaVfS35VTBDncENOpOeyfn6hyAZWQz\r\n"
            + "lpf76pz9Y6ifXNpzBA1M4Z42pE1mFAr0fxIbrp3q/YE1VJQ64fUBDmDaHICSnN8Z1MvywipNJlu5\r\n"
            + "AoGAcxQ2/t6VWPmJ5wSOczGLuT9Oqyus6ev1FBPkZlplC4/TU7sQ+VcGiMZyGZzUm+BgT3Caulyq\r\n"
            + "wQRrMDqRk/bx2FtXyvIdSDUPdF2yGxaWrIee0Mt3KML6TJ0csvL4MoAR7ULt/MAr1Xq2hZT5PFiZ\r\n"
            + "yv3d7sPjJMCaNfQwS7gRsKUCgYA/cZY1JRo0XBcDW1Abt7xlIZmVI8qQmwDEw1t/1sXJuuIKkch0\r\n"
            + "Rjc1o1vdid6i+a8a//4ZcoQTEacKD0z2r8E0s01vKru9QWfhlJRihWrwG97g+IFjOVPpQzK8TinQ\r\n"
            + "pbvEcdfqdaAoQggG6nKY4uAyhHwzu5i1kiu0qVTpsm0JIQKBgF1gcWxNDGEKeHpvMqHXYXN3fg3J\r\n"
            + "Q4za8ku5EOnJE+3fXCxKeH0tQhf9aYVNPioL2JaTWlXB8Ie1A2vvHhfHQ8usemd4OrycosGf1od/\r\n"
            + "rSt9cTngTdbBOlYrF2LbfjQG6oE8MlaDKENhNVNAyF1cJjcmRrT2usRwnLATC9CfoQ0P\r\n"
            + "-----END RSA PRIVATE KEY-----";

}
