package ru.mine.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {

    private String fullName;

    private LocalDate birthDate;

    private String registration;

    private int phoneNumber;

    private int payroll;
}
