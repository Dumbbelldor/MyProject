package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
