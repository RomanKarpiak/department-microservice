package com.roman.exceptions;

public class DepartmentNotEmptyException extends RuntimeException {

    public DepartmentNotEmptyException(Long id) {
        super("Could not dismissal department with id = " + id + " because it is not empty");
    }
}
