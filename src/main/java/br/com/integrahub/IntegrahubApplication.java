package br.com.integrahub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IntegrahubApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrahubApplication.class, args);
    }

}
