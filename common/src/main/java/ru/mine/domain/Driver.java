package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "drivers")
public class Driver implements Serializable {

    private static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "driver_license_id")
    private Long licenseId;

    @Column(name = "driver_license_expiration_date")
    private LocalDate licenseExpDate;

    @Column(name = "available")
    private boolean available;
}
