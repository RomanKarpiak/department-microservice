package com.roman.resource.feign;

import com.roman.resource.feign.fallback.EmployeesFallbackFactory;
import dto.employee.EmployeeFullInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employees-service",fallbackFactory = EmployeesFallbackFactory.class)
public interface EmployeesFeignClient {

    @GetMapping(value = "/employees/department/{departmentId}")
    List<EmployeeFullInfoDto> findByDepartmentId(@PathVariable Long departmentId);
}
