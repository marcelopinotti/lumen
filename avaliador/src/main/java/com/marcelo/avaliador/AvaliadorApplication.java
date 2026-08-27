package com.marcelo.avaliador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AvaliadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvaliadorApplication.class, args);
    }

}
