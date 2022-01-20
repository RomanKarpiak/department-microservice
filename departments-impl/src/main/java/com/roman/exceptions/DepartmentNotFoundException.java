package com.roman.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DepartmentNotFoundException extends RuntimeException {


    public DepartmentNotFoundException(String departmentName) {
        super("Could not find department with name = " + departmentName);
    }

    public DepartmentNotFoundException(Long id) {
        super("Could not find department with id = " + id);
    }
}
