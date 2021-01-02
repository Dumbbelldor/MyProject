package ru.mine.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DriverDTO {

    private Integer employeeId;

    private Integer carId;

    private Long licenseId;

    private Date licenseExpDate;

    private boolean available;
}
