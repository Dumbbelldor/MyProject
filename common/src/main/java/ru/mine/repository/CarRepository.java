package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
