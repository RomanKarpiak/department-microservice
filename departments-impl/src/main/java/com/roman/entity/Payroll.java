package com.roman.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Table(name = "payroll")
@Entity
public class Payroll implements Serializable {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Department department;

    @Column(name = "salary_fund")
    private Long salaryFund;
}
