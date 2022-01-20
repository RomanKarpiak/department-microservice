package com.roman.mappers;

import com.roman.entity.Department;
import com.roman.entity.Payroll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

    @Mapping(target = "id",source = "department.id")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "salaryFund", source = "salaryFund")
    Payroll toEntity(Department department, Long salaryFund);
}
