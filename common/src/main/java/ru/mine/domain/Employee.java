package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "employees")
public class Employee implements Serializable {

    static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column
    private String registration;

    @Column(name = "hiring_date")
    private LocalDate hiringDate;

    @Column
    @Enumerated(EnumType.STRING)
    private SystemRoles position;

    @Column(name = "contract_expiration")
    private LocalDate contractExpiration;

    @Column
    private int payroll;

    @Column(name = "fired")
    private boolean fired;

    @Column(name = "phone_number")
    private int phoneNumber;
}
