package ru.mine.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverDTO {

    private Integer employeeId;

    private Long licenseId;

    private LocalDate licenseExpDate;
}
