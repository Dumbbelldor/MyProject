package ru.mine.service;

import ru.mine.domain.Driver;

import java.util.List;

public interface DriverService {

    List<Driver> findAll();

    Driver save(Driver driver);

    Driver findById(Integer id);

    void deleteById(Integer id);

    List<Driver> findByAvailableTrueAndCarIdNotNull();
}
