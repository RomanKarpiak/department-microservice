package com.roman.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ProducerKafkaTopicConfiguration {

    private final ProducerKafkaProperties producerKafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, producerKafkaProperties.getBootstrapAddress());
        return new KafkaAdmin(config);
    }
    @Bean
    @Qualifier("topic1")
    public NewTopic topic1(){
        return new NewTopic("created-department",1,(short) 1);
    }
    @Bean
    @Qualifier("topic2")
    public NewTopic topic2(){
        return new NewTopic("deleted-department",1,(short) 1);
    }
}
