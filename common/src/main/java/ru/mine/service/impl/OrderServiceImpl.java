package ru.mine.service.impl;

import org.springframework.stereotype.Service;
import ru.mine.domain.Driver;
import ru.mine.domain.Order;
import ru.mine.repository.DriverRepository;
import ru.mine.repository.OrderRepository;
import ru.mine.service.OrderService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final DriverRepository driverRep;

    public OrderServiceImpl(OrderRepository repository, DriverRepository driverRep) {
        this.repository = repository;
        this.driverRep = driverRep;
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public Order findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean isAnyoneReady() {
        return !driverRep.findByAvailableTrue().isEmpty();
    }

    public Integer assignAndGetId(boolean bool) {
        if (bool) {
            List<Driver> readyList = driverRep.findByAvailableTrue();
            Driver driver = readyList.get(
                    ThreadLocalRandom.current().nextInt(readyList.size()));
            driver.setAvailable(false);
            driverRep.save(driver);
            return driver.getId();
        } else {
            List<Driver> unreadyList = driverRep.findAll();
            return unreadyList.get(
                    ThreadLocalRandom.current().nextInt(
                            unreadyList.size())).getId();
        }
    }
}
