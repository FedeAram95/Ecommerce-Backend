package com.backCommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.backCommerce.controller", "com.backCommerce.service", "com.backCommerce.repository", "com.backCommerce.model", "com.backCommerce.config", "com.backCommerce.exception"})
public class BackCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackCommerceApplication.class, args);
    }

}
