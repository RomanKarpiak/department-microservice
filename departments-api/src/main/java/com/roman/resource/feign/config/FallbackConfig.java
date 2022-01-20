package com.roman.resource.feign.config;

import com.roman.resource.feign.fallback.EmployeesFallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FallbackConfig {
    @Bean
    public EmployeesFallbackFactory employeesFallbackFactory(){
        return new EmployeesFallbackFactory();
    }
}
