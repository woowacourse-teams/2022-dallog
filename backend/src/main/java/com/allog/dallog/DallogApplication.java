package com.allog.dallog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DallogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DallogApplication.class, args);
    }

}
