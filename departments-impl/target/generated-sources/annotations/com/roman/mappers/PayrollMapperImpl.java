package com.roman.mappers;

import com.roman.entity.Department;
import com.roman.entity.Payroll;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-05T23:29:31+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_322 (Amazon.com Inc.)"
)
@Component
public class PayrollMapperImpl implements PayrollMapper {

    @Override
    public Payroll toEntity(Department department, Long salaryFund) {
        if ( department == null && salaryFund == null ) {
            return null;
        }

        Payroll payroll = new Payroll();

        if ( department != null ) {
            payroll.setId( department.getId() );
            payroll.setDepartment( department );
        }
        if ( salaryFund != null ) {
            payroll.setSalaryFund( salaryFund );
        }

        return payroll;
    }
}
