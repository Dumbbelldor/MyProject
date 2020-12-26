package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "m_employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column
    private String registration;

    @Column(name = "hiring_date")
    private Date hiringDate;

    @Column
    @Enumerated(EnumType.STRING)
    private SystemRoles position = SystemRoles.EMPLOYEE;

    @Column(name = "contract_expiration")
    private Date contractExpiration;

    @Column
    private int payroll;

    @Column(name = "is_fired")
    private boolean isFired;
}
