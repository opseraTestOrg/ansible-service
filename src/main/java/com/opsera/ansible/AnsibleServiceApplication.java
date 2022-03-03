package com.opsera.ansible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author sreeni
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.opsera.core", "com.opsera.ansible"})
public class AnsibleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnsibleServiceApplication.class, args);
    }
}
