package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "m_cars")
public class Car {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String model;

    @Column
    @Enumerated(EnumType.STRING)
    private CarsColors color = CarsColors.BLACK;

    @Column(name = "license_plate_number")
    private String plateNumber;

    @Column(name = "vin_number")
    private String vin;

    @Column(name = "driver_id")
    private Integer driverId;

    @Column(name = "is_available")
    private boolean isAvailable;
}
