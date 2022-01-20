package com.roman.service;

import com.roman.dto.EmployeeFullInfoDto;
import com.roman.entity.Department;
import com.roman.exceptions.DepartmentAlreadyExistsException;
import com.roman.exceptions.DepartmentHasSubDepartmentException;
import com.roman.exceptions.DepartmentNotEmptyException;
import com.roman.exceptions.DepartmentNotFoundException;
import com.roman.repo.DepartmentRepo;
import com.roman.resource.feign.EmployeesFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final EmployeesFeignClient employeesFeignClient;


    @Override
    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

    @Override
    public Department findById(Long id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Override
    public Department findByDepartmentName(String departmentName) {
        return Optional.ofNullable(departmentRepo.findByDepartmentName(departmentName))
                .orElseThrow(() -> new DepartmentNotFoundException(departmentName));
    }

    @Override
    public boolean delete(Long departmentId) {
        Department department = findById(departmentId);
        List<EmployeeFullInfoDto> employeeList = employeesFeignClient.findByDepartmentId(departmentId);
        if (employeeList.isEmpty() && department.getSubDepartments().isEmpty()) {
            departmentRepo.deleteById(departmentId);
            return true;
        } else if (!employeeList.isEmpty()) {
            throw new DepartmentNotEmptyException(departmentId);
        } else if (!department.getSubDepartments().isEmpty()) {
            throw new DepartmentHasSubDepartmentException(departmentId);
        }
        return false;
    }

    @Override
    public Department create(Department department) {
        String departmentName = department.getDepartmentName();
        if (departmentRepo.findByDepartmentName(departmentName) == null) {
            return departmentRepo.save(department);
        } else {
            throw new DepartmentAlreadyExistsException(departmentName);
        }
    }

    @Override
    public void update(Department newDepartment) {
        departmentRepo.save(newDepartment);
    }

    @Override
    public Set<Department> findAllSubDepartments(Department department) {
        Set<Department> subDepartments = new LinkedHashSet<>();
        Set<Department> tempContainer;
        Queue<Department> queue = new LinkedList<>();
        queue.add(department);
        while (!queue.isEmpty()) {
            Department currentDepartment = queue.poll();
            tempContainer = currentDepartment.getSubDepartments();
            subDepartments.addAll(tempContainer);
            queue.addAll(tempContainer);
        }
        return subDepartments;
    }

    @Override
    public Set<Department> findAllHeadDepartments(Department department) {
        Set<Department> headDepartments = new LinkedHashSet<>();
        Department tempContainer;
        Deque<Department> queue = new ArrayDeque<>();
        queue.add(department);
        while (!queue.isEmpty()) {
            Department currentDepartment = queue.pop();
            tempContainer = currentDepartment.getMainDepartment();
            if (tempContainer == null) {
                break;
            }
            headDepartments.add(tempContainer);
            queue.add(tempContainer);
        }
        return headDepartments;
    }

    @Override
    public Long getSalaryFund(Long id) {
        Department department = findById(id);
        List<EmployeeFullInfoDto> employees = employeesFeignClient.findByDepartmentId(department.getId());
        return employees.stream()
                .mapToLong(EmployeeFullInfoDto::getSalary)
                .sum();
    }

    @Override
    public List<EmployeeFullInfoDto> getEmployeeList(Long id) {
        Department department = findById(id);
        return employeesFeignClient.findByDepartmentId(department.getId());
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepo.findById(id)
                .orElse(null);
    }
}
