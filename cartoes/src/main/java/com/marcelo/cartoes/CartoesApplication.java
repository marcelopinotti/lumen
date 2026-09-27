package com.marcelo.cartoes;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class CartoesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartoesApplication.class, args);
    }

}
