package com.roman.mappers;

import com.roman.dto.DepartmentDto;
import com.roman.dto.DepartmentInfoDto;
import com.roman.entity.Department;
import com.roman.service.DepartmentService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring", uses = EmployeeQualifier.class)
public interface DepartmentMapper {


    @Mapping(target = "id", source = "departmentObject.id")
    @Mapping(target = "departmentName", source = "departmentObject.departmentName")
    @Mapping(target = "creationDate", source = "departmentObject.creationDate")
    @Mapping(target = "headOfDepartment", source = "departmentObject", qualifiedByName = "getHeadOfDepartment")
    @Mapping(target = "quantityOfEmployees", source = "departmentObject", qualifiedByName = "quantityOfEmployees")
    DepartmentInfoDto toInfoDTO(Department departmentObject);

    @Mapping(target = "id", source = "departmentDTO.id")
    @Mapping(target = "departmentName", source = "departmentDTO.departmentName")
    @Mapping(target = "creationDate", source = "departmentDTO.creationDate")
    @Mapping(target = "mainDepartment", source = "departmentDTO", qualifiedByName = "getMainDepartment")
    @Mapping(target = "subDepartments", ignore = true)
    @Mapping(target = "payrollId", ignore = true)
    Department toEntity(DepartmentDto departmentDTO, @Context DepartmentService service);

    @Mapping(target = "id", source = "department.id")
    @Mapping(target = "departmentName", source = "department.departmentName")
    @Mapping(target = "creationDate", source = "department.creationDate")
    @Mapping(target = "mainDepartment", expression = "java(department.getMainDepartment() == null ? null : department.getMainDepartment().getId())")
    DepartmentDto toDTO(Department department);

    @Named("getMainDepartment")
    default Department getMainDepartment(DepartmentDto departmentDTO, @Context DepartmentService service) {
        return departmentDTO.getMainDepartment() == null ? null : service.findById(departmentDTO.getMainDepartment());
    }
}
