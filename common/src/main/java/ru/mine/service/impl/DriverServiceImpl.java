package ru.mine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.mine.domain.Driver;
import ru.mine.repository.DriverRepository;
import ru.mine.service.DriverService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository repository;

    private final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    @Autowired
    public DriverServiceImpl(DriverRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable("drivers")
    public List<Driver> findAll() {
        List<Driver> list = repository.findAll();
        log.info("Found {} drivers", list.size());
        return list;
    }

    @Override
    public Driver save(Driver driver) {
        try {
            Driver saved = repository.save(driver);
            log.info("Driver successfully has been saved");
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("Entity must not be null");
            e.printStackTrace();
            return new Driver();
        }
    }

    @Override
    @Cacheable("drivers")
    public Driver findById(Integer id) {
        try {
            Driver driver = repository.findById(id).orElseThrow();
            log.info("Driver successfully found by id: {}", id);
            return driver;
        } catch (NoSuchElementException e) {
            log.error("Entity is not found by id: {}", id);
            e.printStackTrace();
            return new Driver();
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

    @Override
    public List<Driver> findByAvailableTrueAndCarIdNotNull() {
        try {
            List<Driver> list = repository.findByAvailableTrueAndCarIdNotNull();
            log.info("Found {} available drivers with car", list.size());
            return list;
        } catch (NullPointerException e) {
            log.error("No available drivers to provide");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
