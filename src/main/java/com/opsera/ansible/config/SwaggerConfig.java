/**
 * 
 */
package com.opsera.ansible.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sreeni
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 
     * Swagger UI configuration.
     * 
     * @return
     */
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.opsera.ansible.controller")).paths(PathSelectors.any()).build().apiInfo(apiInfo());
    }

    /**
     * 
     * Update this method for changing the Swagger API title, description and terms
     * of service.
     * 
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Opsera Ansible Service API").description("Opsera API interacting with Ansible").termsOfServiceUrl("https://opsera.io/legal.html").version("1.0").build();
    }

}
