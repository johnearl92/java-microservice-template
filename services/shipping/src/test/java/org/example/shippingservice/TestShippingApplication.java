package org.example.shippingservice;

import org.example.shippingservice.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestShippingApplication {

    public static void main(String[] args) {
        SpringApplication.from(ShippingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
