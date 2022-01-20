package com.roman.resource;

import com.roman.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@RequestMapping(value = "/departments")
public interface DepartmentController {

    @PostMapping
    @ApiOperation(value = "Create a new department")
    @Validated(value = OnCreate.class)
    DepartmentDto create(@Valid @RequestBody DepartmentDto departmentDTO);

    @PutMapping
    @ApiOperation(value = "Update a department")
    @Validated(value = OnUpdate.class)
    DepartmentDto update(@Valid @RequestBody DepartmentDto newDepartment);

    @DeleteMapping("/{departmentId}")
    @ApiOperation(value = "Delete a department by id")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable Long departmentId);

    @GetMapping
    @ApiOperation(value = "Return all departments")
    List<DepartmentInfoDto> findAll();

    @GetMapping(value = "/{departmentId}")
    @ApiOperation(value = "Return a department by id or DepartmentNotFoundException")
    DepartmentInfoDto findById(@PathVariable Long departmentId);

    @GetMapping(value = "/{departmentId}/sub-departments")
    @ApiOperation(value = "Return a list of all sub-departments of a department with departmentId one level below")
    Set<DepartmentInfoDto> findSubDepartments(@PathVariable Long departmentId);

    @GetMapping(value = "/{departmentId}/all-sub-departments")
    @ApiOperation(value = "Return a list of all sub-departments")
    Set<DepartmentInfoDto> findAllSubDepartments(@PathVariable Long departmentId);

    @GetMapping(value = "/name/{departmentName}")
    @ApiOperation(value = "Return a department by name")
    DepartmentInfoDto findByDepartmentName(@PathVariable String departmentName);

    @PutMapping(value = "/{subDepartmentId}/transfer/{headDepartmentId}")
    @ApiOperation(value = "Change a sub-department's main department")
    DepartmentDto transferDepartment(@PathVariable Long subDepartmentId, @PathVariable Long headDepartmentId);

    @GetMapping(value = "/{departmentId}/all-head-departments")
    @ApiOperation(value = "Return a list of all the higher-level departments of a department with departmentId")
    Set<DepartmentInfoDto> findAllHeadDepartments(@PathVariable Long departmentId);

    @GetMapping(value = "/{departmentId}/salary-fund")
    @ApiOperation(value = "Return a payroll of the department")
    Long getSalaryFund(@PathVariable Long departmentId);

    @GetMapping(value = "/{departmentId}/employee-list")
    @ApiOperation(value = "Return a list of department employees")
    List<EmployeeFullInfoDto> getEmployeeList(@PathVariable Long departmentId);

    @GetMapping(value = "/feign-client/{departmentId}")
    @ApiOperation(value = "Return a department by id or null")
    DepartmentDto getDepartmentById(@PathVariable Long departmentId);

}
