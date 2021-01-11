package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "cars")
public class Car implements Serializable {

    private static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "available")
    private boolean available;

    @Column(name = "deleted")
    private boolean deleted;
}
