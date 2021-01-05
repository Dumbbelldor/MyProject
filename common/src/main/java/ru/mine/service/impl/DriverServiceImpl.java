package ru.mine.service.impl;

import org.springframework.stereotype.Service;
import ru.mine.domain.Driver;
import ru.mine.repository.DriverRepository;
import ru.mine.service.DriverService;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository repository;

    public DriverServiceImpl(DriverRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Driver> findAll() {
        return repository.findAll();
    }

    @Override
    public Driver save(Driver driver) {
        return repository.save(driver);
    }

    @Override
    public Driver findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Driver> findByAvailableTrueAndCarIdNotNull() {
        return repository.findByAvailableTrueAndCarIdNotNull();
    }
}
