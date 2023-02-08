package com.epam.esm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class EEJT0004Application {

    public static void main(String[] args) {
        SpringApplication.run(EEJT0004Application.class, args);
    }
}
