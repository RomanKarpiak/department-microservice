package com.roman.component;

import com.roman.entity.Department;
import com.roman.entity.Payroll;
import com.roman.mappers.PayrollMapper;
import com.roman.repo.PayrollRepo;
import com.roman.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ScheduledTask {

    private final DepartmentService service;

    private final PayrollRepo repo;

    private final PayrollMapper mapper;

    @Scheduled(fixedDelayString = "${payrollTracking.delay}")
    public void payrollTracking() {
        log.info("Starting sending the department to the payroll table...");
        List<Department> departmentList = service.findAll();
        for (Department department : departmentList) {
            Long departmentId = department.getId();
            Long salaryFund = service.getSalaryFund(departmentId);
            Payroll payroll = mapper.toEntity(department,salaryFund);
            repo.save(payroll);
        }
        log.info("Sending finished!");
    }
}
