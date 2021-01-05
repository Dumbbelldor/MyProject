package ru.mine.service;

import ru.mine.domain.Car;

import java.util.List;

public interface CarService {

    List<Car> findAll();

    Car save(Car car);

    Car findById(Integer id);

    void deleteById(Integer id);
}
