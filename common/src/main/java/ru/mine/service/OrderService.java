package ru.mine.service;

import ru.mine.domain.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    Order save(Order order);

    Order findById(Integer id);

    void deleteById(Integer id);
}
