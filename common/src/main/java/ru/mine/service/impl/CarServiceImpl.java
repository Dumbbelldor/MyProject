package ru.mine.service.impl;

import org.springframework.stereotype.Service;
import ru.mine.domain.Car;
import ru.mine.repository.CarRepository;
import ru.mine.service.CarService;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repository;

    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public Car save(Car car) {
        return repository.save(car);
    }

    @Override
    public Car findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
