package com.roman;

import com.roman.config.ProducerKafkaProperties;
import com.roman.resource.feign.EmployeesFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = EmployeesFeignClient.class)
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableConfigurationProperties(value = ProducerKafkaProperties.class)
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
