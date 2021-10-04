package com.opsera.ansible.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opsera.ansible.resources.AnsibleClientConstants;

import net.schmizz.sshj.common.IOUtils;

/**
 * 
 * @author sreeni
 * 
 *
 */
public class SystemCommandExecutor {

    

    private final static Logger logger = LoggerFactory.getLogger(SystemCommandExecutor.class);

    boolean stopped = false;

    private SystemCommandExecutor() {

    }

    public static SystemCommandExecutor newExecutor() {
        return new SystemCommandExecutor();
    }

    public List<String> executeCommand(List<String> command, int timeout) throws IOException {
        InputStream inputStream = null;
        InputStream errorStream = null;
        BufferedReader reader = null;
        BufferedReader error_reader = null;
        Process process = null;
        List<String> stdout = new ArrayList<String>();
        try {
            logger.info("execute system command: " + command.stream().collect(Collectors.joining(" ")));
            ProcessBuilder pb = new ProcessBuilder(command);
            process = pb.start();
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            error_reader = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"));
            String outputLine;
            while (!stopped && (outputLine = error_reader.readLine()) != null) {
                stdout.add(outputLine);
                logger.debug(outputLine);
            }
            while (!stopped && (outputLine = reader.readLine()) != null) {
                stdout.add(outputLine);
                logger.debug(outputLine);
            }
        } catch (Exception e) {
            logger.error(AnsibleClientConstants.ERROR_EXECUTE_SYSTEM_COMMAND, e);
        } finally {
            stopped = true;
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(errorStream);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(error_reader);
            if (process != null) {
                try {
                    process.destroyForcibly();
                } catch (Exception e) {

                }
            }
        }
        return stdout;
    }
}
