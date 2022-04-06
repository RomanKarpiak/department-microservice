package com.roman.mappers;

import com.roman.entity.Department;
import com.roman.service.DepartmentService;
import dto.department.DepartmentDto;
import dto.department.DepartmentInfoDto;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-05T23:29:31+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_322 (Amazon.com Inc.)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Autowired
    private EmployeeQualifier employeeQualifier;

    @Override
    public DepartmentInfoDto toInfoDTO(Department departmentObject) {
        if ( departmentObject == null ) {
            return null;
        }

        DepartmentInfoDto departmentInfoDto = new DepartmentInfoDto();

        departmentInfoDto.setId( departmentObject.getId() );
        departmentInfoDto.setDepartmentName( departmentObject.getDepartmentName() );
        departmentInfoDto.setCreationDate( departmentObject.getCreationDate() );
        departmentInfoDto.setHeadOfDepartment( employeeQualifier.getHeadOfDepartment( departmentObject ) );
        departmentInfoDto.setQuantityOfEmployees( employeeQualifier.getQuantityOfEmployees( departmentObject ) );

        return departmentInfoDto;
    }

    @Override
    public Department toEntity(DepartmentDto departmentDTO, DepartmentService service) {
        if ( departmentDTO == null ) {
            return null;
        }

        Department department = new Department();

        department.setId( departmentDTO.getId() );
        department.setDepartmentName( departmentDTO.getDepartmentName() );
        department.setCreationDate( departmentDTO.getCreationDate() );
        department.setMainDepartment( getMainDepartment( departmentDTO, service ) );

        return department;
    }

    @Override
    public DepartmentDto toDTO(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentDto departmentDto = new DepartmentDto();

        departmentDto.setId( department.getId() );
        departmentDto.setDepartmentName( department.getDepartmentName() );
        departmentDto.setCreationDate( department.getCreationDate() );

        departmentDto.setMainDepartment( department.getMainDepartment() == null ? null : department.getMainDepartment().getId() );

        return departmentDto;
    }
}
