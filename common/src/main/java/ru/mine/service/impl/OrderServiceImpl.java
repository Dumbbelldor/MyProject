package ru.mine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mine.domain.Order;
import ru.mine.domain.Driver;
import ru.mine.repository.OrderRepository;
import ru.mine.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final DriverServiceImpl driverRep;

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository repository, DriverServiceImpl driverRep) {
        this.repository = repository;
        this.driverRep = driverRep;
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = repository.findAll();
        log.info("Found {} orders", list.size());
        return list;
    }

    @Override
    public Order save(Order order) {
        try {
            Order saved = repository.save(order);
            log.info("Order successfully has been saved");
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("Entity must not be null");
            e.printStackTrace();
            return new Order();
        }
    }

    @Override
    public Order findById(Integer id) {
        try {
            Order order = repository.findById(id).orElseThrow();
            log.info("Order successfully found by id: {}", id);
            return order;
        } catch (NoSuchElementException e) {
            log.error("Entity is not found by id: {}", id);
            e.printStackTrace();
            return new Order();
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

    public boolean isDeliveryFree(int totalPrice) {
        return totalPrice > 1500;
    }

    public boolean isAnyoneReady() {
        return !driverRep.findByAvailableTrueAndCarIdNotNull().isEmpty();
    }

    public Integer assignAndGetId(boolean bool) {
        if (bool) {
            List<Driver> readyList = driverRep.findByAvailableTrueAndCarIdNotNull();
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
