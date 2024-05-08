package com.softclouds.SpringAdvancedConcepts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringAdvancedConcepts {

    private static final Logger logger = LoggerFactory.getLogger(SpringAdvancedConcepts.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringAdvancedConcepts.class, args);
        logger.info("Application started");
    }

}
