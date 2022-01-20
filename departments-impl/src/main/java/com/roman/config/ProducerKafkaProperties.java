package com.roman.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("kafka")
public class ProducerKafkaProperties {
  private String bootstrapAddress;
}
