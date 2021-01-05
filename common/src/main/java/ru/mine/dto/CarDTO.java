package ru.mine.dto;

import lombok.Data;
import ru.mine.domain.CarsColors;

@Data
public class CarDTO {

    private String model;

    private String plateNumber;

    private String vin;

    private CarsColors color;
}
