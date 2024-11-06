package com.microservice.viajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceViajesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceViajesApplication.class, args);
    }

}
