package com.marcelo.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudgatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudgatewayApplication.class, args);
    }

    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("route1", r -> r.path("/api/clientes/**").uri("lb://clientes"))
                .route("route2", r -> r.path("/api/cartoes/**").uri("lb://cartoes"))
                .route("route3", r -> r.path("/api/avaliador/**").uri("lb://avaliador"))
                .build();
    }

}
