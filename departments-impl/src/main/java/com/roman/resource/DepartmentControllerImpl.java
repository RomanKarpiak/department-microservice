package com.roman.resource;

import com.roman.entity.Department;
import com.roman.mappers.DepartmentMapper;
import com.roman.service.DepartmentService;
import dto.department.DepartmentDto;
import dto.department.DepartmentInfoDto;
import dto.employee.EmployeeFullInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DepartmentControllerImpl implements DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final KafkaTemplate<String, DepartmentDto> kafkaTemplate;

    @Value("${topic1}")
    private String topic1;
    @Value("${topic2}")
    private String topic2;


    @Override
    public DepartmentInfoDto findById(Long departmentId) {
        Department department = departmentService.findById(departmentId);
        return departmentMapper.toInfoDTO(department);
    }

    @Override
    public List<DepartmentInfoDto> findAll() {
        List<Department> departmentList = departmentService.findAll();
        return departmentList.stream()
                .map(departmentMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentInfoDto findByDepartmentName(String departmentName) {
        Department department = departmentService.findByDepartmentName(departmentName);
        return departmentMapper.toInfoDTO(department);
    }

    @Override
    public DepartmentDto create(DepartmentDto newDepartment) {
        Department department = departmentService.create(departmentMapper.toEntity(newDepartment, departmentService));
        DepartmentDto departmentDto = departmentMapper.toDTO(department);
        kafkaTemplate.send(topic1, departmentDto);
        return departmentDto;
    }

    @Override
    public DepartmentDto update(@Valid DepartmentDto updatedDepartment) {
        departmentService.update(departmentMapper.toEntity(updatedDepartment, departmentService));
        return updatedDepartment;
    }

    @Override
    public void deleteById(Long departmentId) {
        Department department = departmentService.findById(departmentId);
        if (departmentService.delete(departmentId)) {
            kafkaTemplate.send(topic2, departmentMapper.toDTO(department));
        }
    }

    @Override
    public Set<DepartmentInfoDto> findAllSubDepartments(Long departmentId) {
        Department department = departmentService.findById(departmentId);
        Set<Department> subDepartments = departmentService.findAllSubDepartments(department);
        return subDepartments.stream()
                .map(departmentMapper::toInfoDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DepartmentInfoDto> findSubDepartments(Long departmentId) {
        Department department = departmentService.findById(departmentId);
        Set<Department> subDepartments = department.getSubDepartments();
        return subDepartments.stream()
                .map(departmentMapper::toInfoDTO)
                .collect(Collectors.toSet());

    }

    @Override
    public DepartmentDto transferDepartment(Long subDepartmentId, Long headDepartmentId) {
        Department subDepartment = departmentService.findById(subDepartmentId);
        Department headDepartment = departmentService.findById(headDepartmentId);
        subDepartment.setMainDepartment(headDepartment);
        departmentService.update(subDepartment);
        return departmentMapper.toDTO(subDepartment);
    }

    @Override
    public Set<DepartmentInfoDto> findAllHeadDepartments(Long departmentId) {
        Department department = departmentService.findById(departmentId);
        Set<Department> headDepartments = departmentService.findAllHeadDepartments(department);
        return headDepartments.stream()
                .map(departmentMapper::toInfoDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Long getSalaryFund(Long departmentId) {
        return departmentService.getSalaryFund(departmentId);
    }

    @Override
    public List<EmployeeFullInfoDto> getEmployeeList(Long departmentId) {
        return departmentService.getEmployeeList(departmentId);
    }

    @Override
    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        return departmentMapper.toDTO(department);
    }

}
