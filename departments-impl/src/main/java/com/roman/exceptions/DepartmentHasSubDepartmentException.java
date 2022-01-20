package com.roman.exceptions;

public class DepartmentHasSubDepartmentException extends RuntimeException {
    public DepartmentHasSubDepartmentException(Long departmentId) {
        super("Can't dismissal Department with id " + departmentId + " because it has subDepartments");
    }
}
