package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table(name = "employees")
public class Employee implements Serializable {

    static final long serialVersionUID = 123L;

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

    @Column(name = "fired")
    private boolean fired;
}
