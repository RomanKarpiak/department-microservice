package com.roman.config;

import com.roman.component.ScheduledTask;
import com.roman.mappers.PayrollMapper;
import com.roman.repo.PayrollRepo;
import com.roman.service.DepartmentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class SchedulingConfiguration {

    @Bean
    @ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
    public ScheduledTask ScheduledTasks(DepartmentService service, PayrollRepo repo, PayrollMapper mapper) {
        return new ScheduledTask(service,repo,mapper);
    }

}

