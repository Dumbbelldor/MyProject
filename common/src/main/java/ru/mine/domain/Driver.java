package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "m_drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "driver_license_id")
    private Long licenseId;

    @Column(name = "driver_license_expiration_date")
    private Date licenseExpDate;

    @Column(name = "is_admitted")
    private boolean isAdmitted;
}
