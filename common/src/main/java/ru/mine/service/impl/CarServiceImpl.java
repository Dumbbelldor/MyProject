package ru.mine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.mine.domain.Car;
import ru.mine.domain.Driver;
import ru.mine.repository.CarRepository;
import ru.mine.service.CarService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repository;

    private final DriverServiceImpl driverRep;

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    @Autowired
    public CarServiceImpl(CarRepository repository, DriverServiceImpl driverRep) {
        this.repository = repository;
        this.driverRep = driverRep;
    }

    @Override
    @Cacheable("cars")
    public List<Car> findAll() {
        List<Car> list = repository.findAll();
        log.info("Found {} cars", list.size());
        return list;
    }

    @Override
    public Car save(Car car) {
        try {
            Car saved = repository.save(car);
            log.info("Car successfully has been saved");
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("Entity must not be null");
            e.printStackTrace();
            return new Car();
        }
    }

    @Override
    @Cacheable("cars")
    public Car findById(Integer id) {
        try {
            Car car = repository.findById(id).orElseThrow();
            log.info("Car successfully found by id: {}", id);
            return car;
        } catch (NoSuchElementException e) {
            log.error("Entity is not found by id: {}", id);
            e.printStackTrace();
            return new Car();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            repository.deleteById(id);
            log.info("Successfully deleted by id: {}", id);
        } catch (IllegalArgumentException e) {
            log.error("Such element cannot be found to perform removal");
            e.printStackTrace();
        }
    }

    public boolean isDriverAllowed(Integer driverId) {
        Driver driver = driverRep.findById(driverId);
        return driver.getLicenseExpDate().isBefore(LocalDate.now());
    }
}
