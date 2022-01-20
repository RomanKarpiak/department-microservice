package com.roman.mappers;

import com.roman.dto.EmployeeFullInfoDto;
import com.roman.entity.Department;
import com.roman.resource.feign.EmployeesFeignClient;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeQualifier {

    private final EmployeesFeignClient feignClient;

    @Named("getHeadOfDepartment")
    public EmployeeFullInfoDto getHeadOfDepartment(Department departmentObject) {
        List<EmployeeFullInfoDto> employeeList = feignClient.findByDepartmentId(departmentObject.getId());
        if (employeeList == null) {
            return null;
        } else {
            return employeeList
                    .stream()
                    .filter(EmployeeFullInfoDto::isHeadOfDepartment)
                    .findFirst()
                    .orElse(null);

        }
    }

    @Named("quantityOfEmployees")
    public int getQuantityOfEmployees(Department departmentObject) {
        List<EmployeeFullInfoDto> employeeList = feignClient.findByDepartmentId(departmentObject.getId());
        return employeeList == null ? 0 : employeeList.size();
    }

}
